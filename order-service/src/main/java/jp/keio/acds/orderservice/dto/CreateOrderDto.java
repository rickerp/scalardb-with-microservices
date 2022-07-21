package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = CreateOrderDto.CreateOrderDtoBuilder.class)
public class CreateOrderDto {
    @JsonProperty("to_id")
    String toId;

    @JsonProperty("order_products")
    List<OrderProductDto> orderProducts;
}
