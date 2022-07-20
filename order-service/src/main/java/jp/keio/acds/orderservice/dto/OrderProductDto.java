package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = OrderProductDto.OrderProductDtoBuilder.class)
public class OrderProductDto {
    @JsonProperty("product")
    GetProductDto product;

    @JsonProperty("count")
    int count;
}

