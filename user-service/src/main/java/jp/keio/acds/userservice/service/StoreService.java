package jp.keio.acds.userservice.service;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import jp.keio.acds.userservice.dto.Store;
import jp.keio.acds.userservice.dto.StoreCreate;
import jp.keio.acds.userservice.dto.StoreUpdate;
import jp.keio.acds.userservice.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoreService extends BaseService {
    private final StoreRepository storeRepo;

    @Autowired
    public StoreService(StoreRepository storeRepo,
                           DistributedTransactionManager manager,
                           TwoPhaseCommitTransactionManager microserviceManager) {
        super(manager, microserviceManager);
        this.storeRepo = storeRepo;
    }

    public Store get(UUID storeId) {
        return this.execute((Transaction<Store>) tx -> storeRepo.getStore(tx, storeId), null);
    }

    public void exists(String storeId, String transactionId) {
        this.execute(tx -> storeRepo.getStore(tx, UUID.fromString(storeId)), transactionId);
    }

    public Store[] listStores() {
        return this.execute(storeRepo::listStores, null);
    }

    public Store createStore(StoreCreate storeIn) {
        return this.execute((Transaction<Store>) tx -> {
            Store store = storeRepo.createStore(tx, storeIn);
            tx.commit();
            return store;
        }, null);
    }

    public Store updateStore(UUID storeId, StoreUpdate storeIn) {
        return this.execute((Transaction<Store>) tx -> {
            Store store = storeRepo.updateStore(tx, storeId, storeIn);
            tx.commit();
            return store;
        }, null);
    }

    public void deleteStore(UUID storeId) {
        this.execute((Transaction<Object>) tx -> {
            storeRepo.deleteStore(tx, storeId);
            tx.commit();
            return null;
        }, null);
    }
}
