package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.exception.transaction.TransactionException;
import jp.keio.acds.userservice.dto.Store;
import jp.keio.acds.userservice.repository.StoreRepository;

public class StoreService extends BaseApiController {
    private static final StoreRepository storeRepo = new StoreRepository();

    public Store[] list() throws TransactionException {
        DistributedTransaction tx = startTransaction();
        System.out.println(tx);
        Store[] stores = storeRepo.list(tx);
        tx.abort();
        return stores;
    }
}
