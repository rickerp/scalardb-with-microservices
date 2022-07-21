package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.*;
import jp.keio.acds.orderservice.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class BaseService {

    private static final int MAX_TRANSACTION_RETRIES = 3;

    private final DistributedTransactionManager manager;

    BaseService(DistributedTransactionManager manager) {
        this.manager = manager;
    }


    public <R> R execute(Transaction<R> transaction) throws InterruptedException {
        int retryCount = 0;

        while (true) {
            DistributedTransaction tx = startTransaction();

            try {
                return transaction.execute(tx);
            } catch (CommitConflictException | CrudConflictException e) {
                retryTransaction(++retryCount);
            } catch (CommitException | CrudException | UnknownTransactionStatusException e) {
                throw new InternalServerErrorException("ERROR : ScalarDB error", e);
            } finally {
                abortTransaction(tx);
            }
        }
    }

    private DistributedTransaction startTransaction() {
        try {
            return manager.start();
        } catch (TransactionException e) {
            throw new InternalServerErrorException("ERROR : Could not start transaction manager", e);
        }
    }

    private void retryTransaction(int retryCount) throws InterruptedException {
        if (retryCount == MAX_TRANSACTION_RETRIES) {
            throw new InternalServerErrorException("ERROR : Failed transaction after 3 retries");
        }
        TimeUnit.MILLISECONDS.sleep(100);
    }

    private void abortTransaction(DistributedTransaction tx) {
        try {
            tx.abort();
        } catch (AbortException ex) {
            log.error(ex.getMessage(), ex);
            throw new InternalServerErrorException("ERROR : Could not abort transaction");
        }
    }
}
