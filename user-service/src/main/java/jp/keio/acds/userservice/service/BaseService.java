package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.*;
import com.scalar.db.service.TransactionFactory;
import jp.keio.acds.userservice.exception.InternalError;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@FunctionalInterface
interface Transaction<R> {
    @Nullable R execute(DistributedTransaction tx) throws TransactionException;
}

public class BaseService {
    private static final int MAX_TRANSACTION_RETRIES = 3;
    public final DistributedTransactionManager transactionManager;

    public BaseService() {
        String SCALARDB_PROPERTIES_PATH = Objects.requireNonNull(getClass().getClassLoader().getResource("scalardb.properties")).getPath();

        TransactionFactory factory = null;
        try {
            factory = TransactionFactory.create(SCALARDB_PROPERTIES_PATH);
        } catch (IOException e) {
            throw new RuntimeException("FAILED TO CREATE TRANSACTION FACTORY");
        }
        this.transactionManager = factory.getTransactionManager();
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

    private DistributedTransaction startTransaction() {
        try {
            return this.transactionManager.start();
        } catch (TransactionException e) {
            throw new InternalError("Error starting ScalarDB transaction", e);
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
}
