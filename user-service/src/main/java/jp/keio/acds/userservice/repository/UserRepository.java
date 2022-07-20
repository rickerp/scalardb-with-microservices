package jp.keio.acds.userservice.repository;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import jp.keio.acds.userservice.dto.User;
import jp.keio.acds.userservice.dto.UserCreate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Repository
public class UserRepository {

    private static final String NAMESPACE = "user-service";
    private static final String TABLE = "user";


    private static final PutBuilder.PartitionKey put = Put.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final GetBuilder.PartitionKeyOrIndexKey get = Get.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final ScanBuilder.PartitionKeyOrIndexKeyOrAll scan = Scan.newBuilder().namespace(NAMESPACE).table(TABLE);

    public User create(DistributedTransaction tx, UserCreate user_in) throws TransactionException {
        String user_id = UUID.randomUUID().toString();
        String now = Instant.now().toString();

        tx.put(put
                .partitionKey(Key.ofText(UserTable.id, user_id))
                .textValue(UserTable.name, user_in.getName())
                .textValue(UserTable.created_at, now)
                .textValue(UserTable.updated_at, now)
                .build()
        );

        return toDto(tx.get(get.partitionKey(Key.ofText(UserTable.id, user_id)).build()).orElseThrow(RuntimeException::new));
    }


    private User toDto(Result r) {
        User user = new User();

        user.setId(UUID.fromString(Objects.requireNonNull(r.getText(StoreTable.id))));
        user.setName(r.getText(StoreTable.name));
        user.setCreatedAt(OffsetDateTime.parse(Objects.requireNonNull(r.getText(StoreTable.created_at))));
        user.setUpdatedAt(OffsetDateTime.parse(Objects.requireNonNull(r.getText(StoreTable.updated_at))));

        return user;
    }
}
