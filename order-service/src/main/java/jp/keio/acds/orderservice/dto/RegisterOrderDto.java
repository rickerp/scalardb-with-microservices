package jp.keio.acds.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = RegisterOrderDto.RegisterOrderDtoBuilder.class)
public class RegisterOrderDto {
    @JsonProperty("transaction_id")
    String txId;
}
