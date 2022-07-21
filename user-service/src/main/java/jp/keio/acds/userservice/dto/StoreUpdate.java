package jp.keio.acds.userservice.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * StoreUpdate
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-22T02:41:53.804+09:00[Asia/Tokyo]")
public class StoreUpdate {

  @JsonProperty("name")
  private String name;

  @JsonProperty("store_type")
  private String storeType;

  public StoreUpdate name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  
  @Schema(name = "name", example = "Miguel Conbini", required = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StoreUpdate storeType(String storeType) {
    this.storeType = storeType;
    return this;
  }

  /**
   * Get storeType
   * @return storeType
  */
  
  @Schema(name = "store_type", example = "CONVENIENCE_STORE", required = false)
  public String getStoreType() {
    return storeType;
  }

  public void setStoreType(String storeType) {
    this.storeType = storeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreUpdate storeUpdate = (StoreUpdate) o;
    return Objects.equals(this.name, storeUpdate.name) &&
        Objects.equals(this.storeType, storeUpdate.storeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, storeType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreUpdate {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    storeType: ").append(toIndentedString(storeType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

