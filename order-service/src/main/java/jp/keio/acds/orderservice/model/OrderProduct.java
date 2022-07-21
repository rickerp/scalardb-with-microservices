package jp.keio.acds.orderservice.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderProduct {
    public static final String ORDER_ID = "order_id";
    public static final String PRODUCT = "product";
    public static final String COUNT = "count";

    String orderId;
    Product product;
    int count;
}
