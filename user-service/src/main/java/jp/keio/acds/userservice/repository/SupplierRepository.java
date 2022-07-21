package jp.keio.acds.userservice.repository;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import jp.keio.acds.userservice.dto.Supplier;
import jp.keio.acds.userservice.dto.SupplierCreate;
import jp.keio.acds.userservice.dto.User;
import jp.keio.acds.userservice.dto.UserCreate;
import jp.keio.acds.userservice.exception.InternalError;
import jp.keio.acds.userservice.exception.NotFound;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

class SupplierTable {
    public static final String id = "id";
    public static final String productType = "product_type";
}

@Repository
public class SupplierRepository {
    private static final UserRepository userRepo = new UserRepository();

    private static final String NAMESPACE = "user-service";
    private static final String TABLE = "supplier";


    private static final PutBuilder.PartitionKey put = Put.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final GetBuilder.PartitionKeyOrIndexKey get = Get.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final ScanBuilder.PartitionKeyOrIndexKeyOrAll scan = Scan.newBuilder().namespace(NAMESPACE).table(TABLE);

    public Supplier get(DistributedTransaction tx, UUID supplierId) throws TransactionException, NotFound {
        User user = userRepo.getUser(tx, supplierId);
        return toDto(
                tx.get(get
                        .partitionKey(Key.ofText(SupplierTable.id, supplierId.toString()))
                        .build()).orElseThrow(NotFound::new),
                user.getName(),
                user.getCreatedAt().toString(),
                user.getUpdatedAt().toString()
        );
    }

    public Supplier[] list(DistributedTransaction tx) throws TransactionException {
        return tx.scan(scan.all().build()).stream().map((result -> {
            User user;
            try {
                user = userRepo.getUser(tx, UUID.fromString(Objects.requireNonNull(result.getText(SupplierTable.id))));
            } catch (NotFound e) {
                throw new InternalError();
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
            return toDto(result, user.getName(), user.getCreatedAt().toString(), user.getUpdatedAt().toString());
        })).toArray(Supplier[]::new);
    }

    public Supplier create(DistributedTransaction tx, SupplierCreate supplierIn) throws TransactionException {
        UserCreate userIn = new UserCreate();
        userIn.name(supplierIn.getName());
        User user = userRepo.createStore(tx, userIn);

        tx.put(put
                .partitionKey(Key.ofText(SupplierTable.id, user.getId().toString()))
                .textValue(SupplierTable.productType, supplierIn.getProductType())
                .build()
        );

        return this.get(tx, user.getId());
    }

    private Supplier toDto(Result result, String name, String createdAt, String updatedAt) {
        Supplier supplier = new Supplier();

        supplier.setId(UUID.fromString(Objects.requireNonNull(result.getText(SupplierTable.id))));
        supplier.setProductType(result.getText(SupplierTable.productType));
        supplier.setName(name);
        supplier.setCreatedAt(OffsetDateTime.parse(createdAt));
        supplier.setUpdatedAt(OffsetDateTime.parse(updatedAt));

        return supplier;
    }
}
