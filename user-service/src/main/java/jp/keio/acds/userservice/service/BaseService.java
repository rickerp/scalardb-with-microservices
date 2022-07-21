package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.service.TransactionFactory;

import java.io.IOException;
import java.util.Objects;

public class BaseService {
    private final DistributedTransactionManager transactionManager;

    public BaseService() {
        String SCALARDB_PROPERTIES_PATH = Objects.requireNonNull(getClass().getClassLoader().getResource("scalardb.properties")).getPath();
        System.out.println(SCALARDB_PROPERTIES_PATH);
        TransactionFactory factory = null;
        try {
            factory = TransactionFactory.create(SCALARDB_PROPERTIES_PATH);
        } catch (IOException e) {
            throw new RuntimeException("FAILED TO CREATE TRANSACTION FACTORY");
        }
        this.transactionManager = factory.getTransactionManager();
    }

    protected DistributedTransaction startTransaction() throws TransactionException {
        return this.transactionManager.start();
    }
}
