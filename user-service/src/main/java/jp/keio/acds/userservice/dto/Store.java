package jp.keio.acds.userservice.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Store
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-21T01:15:32.795+09:00[Asia/Tokyo]")
public class Store {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("store_type")
  private String storeType;

  @JsonProperty("created_at")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @JsonProperty("updated_at")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  public Store id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @NotNull @Valid 
  @Schema(name = "id", required = true)
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Store name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull 
  @Schema(name = "name", example = "Miguels Conbini", required = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Store storeType(String storeType) {
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

  public Store createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @Valid 
  @Schema(name = "created_at", required = false)
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Store updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
  */
  @Valid 
  @Schema(name = "updated_at", required = false)
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Store store = (Store) o;
    return Objects.equals(this.id, store.id) &&
        Objects.equals(this.name, store.name) &&
        Objects.equals(this.storeType, store.storeType) &&
        Objects.equals(this.createdAt, store.createdAt) &&
        Objects.equals(this.updatedAt, store.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, storeType, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Store {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    storeType: ").append(toIndentedString(storeType)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

