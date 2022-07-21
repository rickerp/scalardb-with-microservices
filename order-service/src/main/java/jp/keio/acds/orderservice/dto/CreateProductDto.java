package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonDeserialize(builder = CreateProductDto.CreateProductDtoBuilder.class)
public class CreateProductDto {
    @JsonProperty("owner_id")
    String ownerId;

    @JsonProperty("name")
    String name;

    @JsonProperty("price")
    Double price;
}
