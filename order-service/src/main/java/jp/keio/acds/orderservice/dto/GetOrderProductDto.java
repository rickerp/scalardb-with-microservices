package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = GetOrderProductDto.GetOrderProductDtoBuilder.class)
public class GetOrderProductDto {
    @JsonProperty("order_id")
    String orderId;

    @JsonProperty("product_id")
    String productId;

    @JsonProperty("count")
    int count;
}

