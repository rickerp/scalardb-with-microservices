package jp.keio.acds.orderservice.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Product {
    public static final String PRODUCT_ID = "id";
    public static final String NAME = "name";
    public static final String PRICE = "count";

    String productId;
    String name;
    int price;
}
