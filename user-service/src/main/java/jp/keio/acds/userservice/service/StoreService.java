package jp.keio.acds.userservice.service;

import jp.keio.acds.userservice.dto.Store;
import jp.keio.acds.userservice.dto.StoreCreate;
import jp.keio.acds.userservice.dto.StoreUpdate;
import jp.keio.acds.userservice.repository.StoreRepository;

import java.util.UUID;

public class StoreService extends BaseService {
    private static final StoreRepository storeRepo = new StoreRepository();

    public Store get(UUID storeId) {
        return this.execute((Transaction<Store>) tx -> storeRepo.getStore(tx, storeId), null);
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
