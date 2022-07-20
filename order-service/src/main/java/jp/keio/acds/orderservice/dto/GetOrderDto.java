package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonDeserialize(builder = GetOrderDto.GetOrderDtoBuilder.class)
public class GetOrderDto {
    @JsonProperty("id")
    String orderId;

    @JsonProperty("from_id")
    String fromId;

    @JsonProperty("to_id")
    String toId;

    @JsonProperty("timestamp")
    String timestamp;

    @JsonProperty("order_products")
    List<OrderProductDto> orderProducts;
}
