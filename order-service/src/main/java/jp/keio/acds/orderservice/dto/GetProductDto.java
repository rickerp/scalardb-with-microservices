package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = ProductDto.ProductDtoBuilder.class)
public class ProductDto {
    @JsonProperty("name")
    String name;

    @JsonProperty("price")
    double price;
}

