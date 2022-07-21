package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransaction;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import com.scalar.db.exception.transaction.*;
import com.scalar.db.service.TransactionFactory;
import jp.keio.acds.userservice.exception.InternalError;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@FunctionalInterface
interface Transaction<R> {
    @Nullable R execute(DistributedTransaction tx) throws TransactionException;
}

@FunctionalInterface
interface MicroserviceTransaction<R> {
    @Nullable R execute(TwoPhaseCommitTransaction tx) throws TransactionException;
}

public class BaseService {
    private static final int MAX_TRANSACTION_RETRIES = 3;
    public final DistributedTransactionManager transactionManager;

    public final TwoPhaseCommitTransactionManager microserviceTransactionManager;

    public BaseService() {
        String SCALARDB_PROPERTIES_PATH = Objects.requireNonNull(getClass().getClassLoader().getResource("scalardb.properties")).getPath();

        TransactionFactory factory;
        try {
            factory = TransactionFactory.create(SCALARDB_PROPERTIES_PATH);
        } catch (IOException e) {
            throw new RuntimeException("FAILED TO CREATE TRANSACTION FACTORY");
        }
        this.transactionManager = factory.getTransactionManager();
        this.microserviceTransactionManager = factory.getTwoPhaseCommitTransactionManager();
    }

    protected <R> R execute(Transaction<R> transaction, DistributedTransaction dtx) {
        int retryCount = 0;

        while (true) {
            DistributedTransaction tx = dtx == null ? startTransaction() : dtx;

            try {
                return transaction.execute(tx);
            } catch (CommitConflictException | CrudConflictException e) {
                retryTransaction(++retryCount);
            } catch (TransactionException e) {
                throw new InternalError("ScalarDB Error", e);
            } finally {
                abortTransaction(tx);
            }
        }
    }

    protected <R> R execute(MicroserviceTransaction<R> transaction, UUID transactionId) {
        int retryCount = 0;

        while (true) {
            TwoPhaseCommitTransaction tx = resumeTransaction(transactionId.toString());

            try {
                return transaction.execute(tx);
            } catch (CrudConflictException e) {
                retryTransaction(++retryCount);
            } catch (TransactionException e) {
                throw new InternalError("ScalarDB Error", e);
            } finally {
                suspendTransaction(tx);
            }
        }
    }

    private DistributedTransaction startTransaction() {
        try {
            return this.transactionManager.start();
        } catch (TransactionException e) {
            throw new InternalError("Error starting ScalarDB transaction", e);
        }
    }

    private TwoPhaseCommitTransaction startMicroserviceTransaction() {
        try {
            return microserviceTransactionManager.start();
        } catch (TransactionException e) {
            throw new InternalError("Error, could not start two phase commit transaction manager", e);
        }
    }

    private void retryTransaction(int retryCount) {
        if (retryCount == MAX_TRANSACTION_RETRIES) {
            throw new InternalError("Max retries reached while retrying to execute transaction");
        }
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new InternalError("Thread sleep error", e);
        }
    }


    private void abortTransaction(DistributedTransaction tx) {
        try {
            tx.abort();
        } catch (AbortException ex) {
            throw new InternalError("Error, could not abort transaction");
        }
    }

    public void joinTransaction(UUID transactionId) {
        try {
            TwoPhaseCommitTransaction tx = microserviceTransactionManager.join(transactionId.toString());
            suspendTransaction(tx);
        } catch (TransactionException e) {
            throw new InternalError("Error, could not join transaction");
        }
    }

    public void prepareTransaction(UUID transactionId) {
        TwoPhaseCommitTransaction tx = resumeTransaction(transactionId.toString());

        try {
            tx.prepare();
            suspendTransaction(tx);
        } catch (PreparationException e) {
            throw new InternalError("Error, could not prepare transaction");
        }
    }

    public void commitTransaction(UUID transactionId) {
        TwoPhaseCommitTransaction tx = resumeTransaction(transactionId.toString());

        try {
            tx.commit();
        } catch (CommitException e) {
            suspendTransaction(tx);
            throw new InternalError("Error, could not commit transaction");
        } catch (UnknownTransactionStatusException e) {
            suspendTransaction(tx);
            throw new InternalError("Error, unknown transaction status while committing");
        }
    }

    public void rollbackTransaction(UUID transactionId) {
        try {
            TwoPhaseCommitTransaction tx = resumeTransaction(transactionId.toString());
            tx.rollback();
        } catch (RollbackException ex) {
            throw new InternalError("Error, could not rollback transaction");
        }
    }

    private TwoPhaseCommitTransaction resumeTransaction(String transactionId) {
        try {
            return microserviceTransactionManager.resume(transactionId);
        } catch (TransactionException e) {
            throw new InternalError("Error, could not resume transaction");
        }
    }

    private void suspendTransaction(TwoPhaseCommitTransaction tx) {
        try {
            microserviceTransactionManager.suspend(tx);
        } catch (TransactionException e) {
            throw new InternalError("Error, could not suspend transaction");
        }
    }
}
