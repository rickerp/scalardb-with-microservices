package jp.keio.acds.orderservice.repository;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.api.TwoPhaseCommitTransaction;
import com.scalar.db.exception.transaction.*;
import com.scalar.db.io.Key;
import jp.keio.acds.orderservice.dto.GetOrderDto;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepository extends BaseRepository<GetOrderDto> {

    private static final String NAMESPACE = "order-service";
    private static final String TABLE_NAME = "order";
    private static final String ORDER_ID = "id";
    private static final String FROM_ID = "from_id";
    private static final String TO_ID = "to_id";
    private static final String TIMESTAMP = "timestamp";


    public String createOrder(TwoPhaseCommitTransaction tx, String fromID, String toId) throws CrudException {
        String orderId = UUID.randomUUID().toString();

        Put put = Put.newBuilder()
                .namespace(NAMESPACE)
                .table(TABLE_NAME)
                .partitionKey(Key.ofText(ORDER_ID, orderId))
                .textValue(FROM_ID, fromID)
                .textValue(TO_ID, toId)
                .textValue(TIMESTAMP, Instant.now().toString())
                .build();

        tx.put(put);
        return orderId;
    }

    public GetOrderDto getOrder(DistributedTransaction tx, String orderId) throws CrudException {
        return get(tx, orderId);
    }

    public List<GetOrderDto> listOrders(DistributedTransaction tx) throws CrudException {
        return scan(tx);
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

    protected GetOrderDto toDto(Result result) {
        return GetOrderDto.builder()
                .orderId(result.getText(ORDER_ID))
                .fromId(result.getText(FROM_ID))
                .toId(result.getText(TO_ID))
                .timestamp(result.getText(TIMESTAMP))
                .build();
    }
}
