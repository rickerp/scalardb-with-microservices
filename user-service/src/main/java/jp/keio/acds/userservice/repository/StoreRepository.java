package jp.keio.acds.userservice.repository;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import jp.keio.acds.userservice.dto.*;
import jp.keio.acds.userservice.exception.InternalError;
import jp.keio.acds.userservice.exception.NotFound;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;


class StoreTable {
    public static final String id = "id";
    public static final String storeType = "store_type";
}

@Repository
public class StoreRepository {
    private static final UserRepository userRepo = new UserRepository();

    private static final String NAMESPACE = "user-service";
    private static final String TABLE = "store";

    private static final DeleteBuilder.PartitionKey delete = Delete.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final PutBuilder.PartitionKey put = Put.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final GetBuilder.PartitionKeyOrIndexKey get = Get.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final ScanBuilder.PartitionKeyOrIndexKeyOrAll scan = Scan.newBuilder().namespace(NAMESPACE).table(TABLE);

    public Store getStore(DistributedTransaction tx, UUID storeId) throws TransactionException, NotFound {
        User user = userRepo.getUser(tx, storeId);
        return toDto(
                tx.get(get
                        .partitionKey(Key.ofText(StoreTable.id, storeId.toString()))
                        .build()).orElseThrow(NotFound::new),
                user.getName(),
                user.getCreatedAt().toString(),
                user.getUpdatedAt().toString()
        );
    }

    public Store[] listStores(DistributedTransaction tx) throws TransactionException {
        return tx.scan(scan.all().build()).stream().map((result -> {
            User user;
            try {
                user = userRepo.getUser(tx, UUID.fromString(Objects.requireNonNull(result.getText(StoreTable.id))));
            } catch (NotFound e) {
                throw new InternalError();
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
            return toDto(result, user.getName(), user.getCreatedAt().toString(), user.getUpdatedAt().toString());
        })).toArray(Store[]::new);
    }

    public Store createStore(DistributedTransaction tx, StoreCreate storeIn) throws TransactionException {
        UserCreate userIn = new UserCreate();
        userIn.name(storeIn.getName());
        User user = userRepo.createStore(tx, userIn);

        tx.put(put
                .partitionKey(Key.ofText(StoreTable.id, user.getId().toString()))
                .textValue(StoreTable.storeType, storeIn.getStoreType())
                .build()
        );

        return this.getStore(tx, user.getId());
    }

    public Store updateStore(DistributedTransaction tx, UUID storeId, StoreUpdate storeIn) throws TransactionException {
        UserUpdate userIn = new UserUpdate();
        userIn.setName(storeIn.getName());
        User user = userRepo.updateStore(tx, storeId, userIn);

        PutBuilder.Buildable putQuery = put.partitionKey(Key.ofText(StoreTable.id, storeId.toString()));
        if (storeIn.getStoreType() != null) {
            putQuery.textValue(StoreTable.storeType, storeIn.getStoreType());
        }
        tx.put(putQuery.build());
        return this.getStore(tx, storeId);
    }

    public void deleteStore(DistributedTransaction tx, UUID storeId) throws TransactionException {
        tx.delete(delete.partitionKey(Key.ofText(StoreTable.id, storeId.toString())).build());
//        userRepo.deleteUser(tx, storeId);
    }

    private Store toDto(Result result, String name, String createdAt, String updatedAt) {
        Store store = new Store();

        store.setId(UUID.fromString(Objects.requireNonNull(result.getText(StoreTable.id))));
        store.setStoreType(result.getText(StoreTable.storeType));
        store.setName(name);
        store.setCreatedAt(OffsetDateTime.parse(createdAt));
        store.setUpdatedAt(OffsetDateTime.parse(updatedAt));

        return store;
    }
}
