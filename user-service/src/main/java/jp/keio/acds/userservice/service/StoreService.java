package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.exception.transaction.TransactionException;
import jp.keio.acds.userservice.dto.Store;
import jp.keio.acds.userservice.dto.StoreCreate;
import jp.keio.acds.userservice.repository.StoreRepository;

import java.util.UUID;

public class StoreService extends BaseApiController {
    private static final StoreRepository storeRepo = new StoreRepository();

    public Store get(UUID storeId) throws TransactionException {
        DistributedTransaction tx = startTransaction();
        Store store = storeRepo.get(tx, storeId);
        tx.abort();
        return store;
    }

    public Store[] list() throws TransactionException {
        DistributedTransaction tx = startTransaction();
        Store[] stores = storeRepo.list(tx);
        tx.abort();
        return stores;
    }

    public Store create(StoreCreate storeIn) throws TransactionException {
        DistributedTransaction tx = startTransaction();
        Store store = storeRepo.create(tx, storeIn);
        tx.abort();
        return store;
    }
}
