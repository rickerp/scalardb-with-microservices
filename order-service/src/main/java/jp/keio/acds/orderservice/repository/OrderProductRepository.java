package jp.keio.acds.orderservice.repository;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.api.TwoPhaseCommitTransaction;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.io.Key;
import jp.keio.acds.orderservice.dto.GetOrderProductDto;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductRepository extends BaseRepository<GetOrderProductDto> {

    private static final String NAMESPACE = "order-service";
    private static final String TABLE_NAME = "order_product";
    private static final String ORDER_ID = "order_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String COUNT = "count";


    public void createOrderProduct(TwoPhaseCommitTransaction tx, String orderId, String productId, Integer count)
            throws CrudException {
        Put put = Put.newBuilder()
                .namespace(NAMESPACE)
                .table(TABLE_NAME)
                .partitionKey(Key.ofText(ORDER_ID, orderId))
                .clusteringKey(Key.ofText(PRODUCT_ID, productId))
                .intValue(COUNT, count)
                .build();

        tx.put(put);
    }

    @Override
    protected String getNamespace() {
        return NAMESPACE;
    }

    @Override
    protected String getTable() {
        return TABLE_NAME;
    }

    @Override
    protected String getPartitionKey() {
        return ORDER_ID;
    }

    protected String getClusteringKey() {
        return PRODUCT_ID;
    }

    protected GetOrderProductDto toDto(Result result) {
        return GetOrderProductDto.builder()
                .orderId(result.getText(ORDER_ID))
                .productId(result.getText(PRODUCT_ID))
                .count(result.getInt(COUNT))
                .build();
    }
}
