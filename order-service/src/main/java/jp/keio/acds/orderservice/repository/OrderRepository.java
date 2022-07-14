package jp.keio.acds.orderservice.repository;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.*;
import com.scalar.db.io.Key;
import jp.keio.acds.orderservice.dto.OrderDto;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class OrderRepository {

    private static final String NAMESPACE = "orderservice";
    private static final String TABLE_NAME = "order";
    private static final String ORDER_ID = "id";
    private static final String FROM_ID = "from_id";
    private static final String TO_ID = "to_id";
    private static final String TIMESTAMP = "timestamp";


    public String createOrder(DistributedTransaction tx, OrderDto orderDto) throws CrudException {
        String orderId = UUID.randomUUID().toString();

        //TODO check if order_id already exists
        Put put = Put.newBuilder()
                .namespace(NAMESPACE)
                .table(TABLE_NAME)
                .partitionKey(Key.ofText(ORDER_ID, orderId))
                .intValue(FROM_ID, orderDto.getFromId())
                .intValue(TO_ID, orderDto.getToId())
                .bigIntValue(TIMESTAMP, System.currentTimeMillis())
                .build();

        tx.put(put);
        return orderId;
    }

    public OrderDto getOrder(String orderId, DistributedTransaction tx) throws AbortException {
        return null;
    }
}
