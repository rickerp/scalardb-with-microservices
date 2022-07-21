package jp.keio.acds.orderservice.service;

import com.scalar.db.api.TwoPhaseCommitTransaction;
import com.scalar.db.exception.transaction.CommitException;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.exception.transaction.PreparationException;
import com.scalar.db.exception.transaction.UnknownTransactionStatusException;

@FunctionalInterface
public interface MicroserviceTransaction<R> {
    R execute(TwoPhaseCommitTransaction tx)
            throws CrudException, CommitException, PreparationException, UnknownTransactionStatusException;
}
