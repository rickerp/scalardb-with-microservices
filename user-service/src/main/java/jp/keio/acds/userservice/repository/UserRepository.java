package jp.keio.acds.userservice.repository;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import jp.keio.acds.userservice.dto.User;
import jp.keio.acds.userservice.dto.UserCreate;
import jp.keio.acds.userservice.dto.UserUpdate;
import jp.keio.acds.userservice.exception.NotFound;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;


class UserTable {
    public static final String id = "id";
    public static final String name = "name";
    public static final String createdAt = "created_at";
    public static final String updatedAt = "updated_at";
}

@Repository
public class UserRepository {

    private static final String NAMESPACE = "user-service";
    private static final String TABLE = "user";

    private static final DeleteBuilder.PartitionKey delete = Delete.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final PutBuilder.PartitionKey put = Put.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final GetBuilder.PartitionKeyOrIndexKey get = Get.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final ScanBuilder.PartitionKeyOrIndexKeyOrAll scan = Scan.newBuilder().namespace(NAMESPACE).table(TABLE);

    public User getUser(TransactionCrudOperable tx, UUID userId) throws TransactionException, NotFound {
        return toDto(tx.get(get.partitionKey(Key.ofText(UserTable.id, userId.toString())).build()).orElseThrow(NotFound::new));
    }

    public User createStore(DistributedTransaction tx, UserCreate userIn) throws TransactionException {
        UUID userId = UUID.randomUUID();
        String now = Instant.now().toString();

        tx.put(put
                .partitionKey(Key.ofText(UserTable.id, userId.toString()))
                .textValue(UserTable.name, userIn.getName())
                .textValue(UserTable.createdAt, now)
                .textValue(UserTable.updatedAt, now)
                .build()
        );
        return this.getUser(tx, userId);
    }

    public User updateStore(DistributedTransaction tx, UUID userId, UserUpdate userIn) throws TransactionException {
        PutBuilder.Buildable putQuery = put.partitionKey(Key.ofText(UserTable.id, userId.toString()));
        if (userIn.getName() != null) {
            putQuery = putQuery.textValue(UserTable.name, userIn.getName());
        }
        tx.put(putQuery.build());
        return this.getUser(tx, userId);
    }

    public Delete deleteUserQuery(UUID userId) {
        return delete.partitionKey(Key.ofText(StoreTable.id, userId.toString())).build();
    }

    public void deleteUser(DistributedTransaction tx, UUID userId) throws TransactionException {
        tx.delete(deleteUserQuery(userId));
    }


    private User toDto(Result result) {
        User user = new User();

        user.setId(UUID.fromString(Objects.requireNonNull(result.getText(UserTable.id))));
        user.setName(result.getText(UserTable.name));
        user.setCreatedAt(OffsetDateTime.parse(Objects.requireNonNull(result.getText(UserTable.createdAt))));
        user.setUpdatedAt(OffsetDateTime.parse(Objects.requireNonNull(result.getText(UserTable.updatedAt))));

        return user;
    }
}
