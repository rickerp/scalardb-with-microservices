package jp.keio.acds.orderservice.model;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Order {
    public static final String ORDER_ID = "id";
    public static final String FROM_ID = "from_id";
    public static final String TO_ID = "to_id";
    public static final String TIMESTAMP = "timestamp";
    public static final String ORDER_PRODUCTS = "order_products";

    String id;
    String fromId;
    String toId;
    Long timestamp;
    List<OrderProduct> orderProducts;
}
