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
 * SupplierCreate
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-21T11:41:32.293+09:00[Asia/Tokyo]")
public class SupplierCreate {

  @JsonProperty("name")
  private String name;

  @JsonProperty("product_type")
  private String productType;

  public SupplierCreate name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull 
  @Schema(name = "name", required = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SupplierCreate productType(String productType) {
    this.productType = productType;
    return this;
  }

  /**
   * Get productType
   * @return productType
  */
  
  @Schema(name = "product_type", required = false)
  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupplierCreate supplierCreate = (SupplierCreate) o;
    return Objects.equals(this.name, supplierCreate.name) &&
        Objects.equals(this.productType, supplierCreate.productType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, productType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SupplierCreate {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    productType: ").append(toIndentedString(productType)).append("\n");
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

