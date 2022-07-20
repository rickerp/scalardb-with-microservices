package jp.keio.acds.orderservice.repository;

import com.scalar.db.api.*;
import com.scalar.db.exception.transaction.*;
import com.scalar.db.io.Key;
import jp.keio.acds.orderservice.dto.CreateOrderDto;
import jp.keio.acds.orderservice.dto.GetOrderDto;
import jp.keio.acds.orderservice.exception.OrderNotFoundException;
import jp.keio.acds.orderservice.model.Order;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class OrderRepository {

    private static final String NAMESPACE = "order-service";
    private static final String TABLE_NAME = "order";
    private static final String ORDER_ID = "id";
    private static final String FROM_ID = "from_id";
    private static final String TO_ID = "to_id";
    private static final String TIMESTAMP = "timestamp";


    public String createOrder(DistributedTransaction tx, CreateOrderDto createOrderDto) throws CrudException {
        String orderId = UUID.randomUUID().toString();

        //TODO check if order_id already exists
        Put put = Put.newBuilder()
                .namespace(NAMESPACE)
                .table(TABLE_NAME)
                .partitionKey(Key.ofText(ORDER_ID, orderId))
                .textValue(FROM_ID, createOrderDto.getFromId())
                .textValue(TO_ID, createOrderDto.getToId())
                .textValue(TIMESTAMP, Instant.now().toString())
                .build();

        tx.put(put);
        return orderId;
    }

    public GetOrderDto getOrder(DistributedTransaction tx, String orderId) throws CrudException {
        Get get = Get.newBuilder()
                .namespace(NAMESPACE)
                .table(TABLE_NAME)
                .partitionKey(Key.ofText(ORDER_ID, orderId))
                .build();

        return toDto(tx.get(get)
                .orElseThrow(
                        () -> new OrderNotFoundException("ERROR : Order with ID " + orderId + " does not exist")));
    }

    private GetOrderDto toDto(Result result) {
        return GetOrderDto.builder()
                .orderId(result.getText(ORDER_ID))
                .fromId(result.getText(FROM_ID))
                .toId(result.getText(TO_ID))
                .timestamp(result.getText(TIMESTAMP))
                .build();
    }
}
