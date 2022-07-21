package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.exception.transaction.CommitException;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.exception.transaction.UnknownTransactionStatusException;

@FunctionalInterface
public interface Transaction<R> {
    R execute(DistributedTransaction tx) throws CrudException, CommitException, UnknownTransactionStatusException;
}
