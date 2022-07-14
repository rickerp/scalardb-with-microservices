package jp.keio.acds.userservice.repository;

import com.scalar.db.api.*;

import com.scalar.db.exception.transaction.TransactionException;
import jp.keio.acds.userservice.dto.Store;
import org.springframework.stereotype.Repository;


@Repository
public class StoreRepository extends UserRepository {
    private static final String NAMESPACE = "userservice";
    private static final String TABLE = "store";


    private static final PutBuilder.PartitionKey put = Put.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final GetBuilder.PartitionKeyOrIndexKey get = Get.newBuilder().namespace(NAMESPACE).table(TABLE);
    private static final ScanBuilder.PartitionKeyOrIndexKeyOrAll scan = Scan.newBuilder().namespace(NAMESPACE).table(TABLE);

    public Store[] listStores(DistributedTransaction transaction) throws TransactionException {
        return transaction.scan(scan.all().build()).stream().map(Store::fromResult).toArray(Store[]::new);
    }

}
