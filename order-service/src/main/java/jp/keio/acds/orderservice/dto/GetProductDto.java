package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = GetProductDto.GetProductDtoBuilder.class)
public class GetProductDto {
    @JsonProperty("product_id")
    String productId;

    @JsonProperty("owner_id")
    String ownerId;

    @JsonProperty("name")
    String name;

    @JsonProperty("price")
    double price;
}

