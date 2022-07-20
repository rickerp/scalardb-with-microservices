package jp.keio.acds.userservice.repository;

import com.scalar.db.api.*;

import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import jp.keio.acds.userservice.dto.Store;
import jp.keio.acds.userservice.table.StoreTable;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Repository
public class StoreRepository extends UserRepository {
    private static final UserRepository userRepo = new UserRepository();

    private static final String NAMESPACE = "user-service";
    private static final String TABLE = "store";


    private static final PutBuilder.PartitionKey put = Put.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final GetBuilder.PartitionKeyOrIndexKey get = Get.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final ScanBuilder.PartitionKeyOrIndexKeyOrAll scan = Scan.newBuilder().namespace(NAMESPACE).table(TABLE);

    public Store[] list(DistributedTransaction tx) throws TransactionException {
        return tx.scan(scan.all().build()).stream().map(this::toDto).toArray(Store[]::new);
    }

    public Store create(DistributedTransaction tx) throws TransactionException {


        Key store_id = Key.ofText(StoreTable.id, UUID.randomUUID().toString());
        tx.put(put.partitionKey(store_id).);
    }

    private Store toDto(Result r) {
        Store store = new Store();

        store.setId(UUID.fromString(Objects.requireNonNull(r.getText(StoreTable.id))));
        store.setName(r.getText(StoreTable.name));
        store.setCreatedAt(OffsetDateTime.parse(Objects.requireNonNull(r.getText(StoreTable.created_at))));
        store.setUpdatedAt(OffsetDateTime.parse(Objects.requireNonNull(r.getText(StoreTable.updated_at))));

        return store;
    }
}
