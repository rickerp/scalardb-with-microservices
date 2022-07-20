package jp.keio.acds.userservice.repository;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import jp.keio.acds.userservice.dto.User;
import jp.keio.acds.userservice.dto.UserCreate;
import jp.keio.acds.userservice.exception.NotFound;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private static final String NAMESPACE = "user-service";
    private static final String TABLE = "user";


    private static final PutBuilder.PartitionKey put = Put.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final GetBuilder.PartitionKeyOrIndexKey get = Get.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final ScanBuilder.PartitionKeyOrIndexKeyOrAll scan = Scan.newBuilder().namespace(NAMESPACE).table(TABLE);

    public User get(DistributedTransaction tx, UUID userId) throws TransactionException, NotFound {
        return toDto(tx.get(get.partitionKey(Key.ofText(UserTable.id, userId.toString())).build()).orElseThrow(NotFound::new));
    }

    public User create(DistributedTransaction tx, UserCreate userIn) throws TransactionException {
        String userId = UUID.randomUUID().toString();
        String now = Instant.now().toString();

        tx.put(put
                .partitionKey(Key.ofText(UserTable.id, userId))
                .textValue(UserTable.name, userIn.getName())
                .textValue(UserTable.createdAt, now)
                .textValue(UserTable.updatedAt, now)
                .build()
        );

        return toDto(tx.get(get.partitionKey(Key.ofText(UserTable.id, userId)).build()).orElseThrow(RuntimeException::new));
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
