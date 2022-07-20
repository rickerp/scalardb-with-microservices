package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = OrderDto.OrderDtoBuilder.class)
public class OrderDto {
    @JsonProperty("from_id")
    String fromId;

    @JsonProperty("to_id")
    String toId;

    @JsonProperty("order_products")
    List<OrderProductDto> orderProducts;
}
