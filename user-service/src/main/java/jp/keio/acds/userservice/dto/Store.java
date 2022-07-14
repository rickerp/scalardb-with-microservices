package jp.keio.acds.userservice.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.scalar.db.api.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import jp.keio.acds.userservice.repository.StoreRepository;
import jp.keio.acds.userservice.table.StoreTable;


import javax.annotation.Generated;

/**
 * Store
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-07-14T21:26:15.337235+09:00[Asia/Tokyo]")
public class Store {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    public Store id(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @NotNull
    @Valid
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
     *
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
                Objects.equals(this.name, store.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Store {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

    public static Store fromResult(Result r) {
        Store store = new Store();

        store.setId(UUID.fromString(r.getText(StoreTable.id)));
        store.setName(r.getText(StoreTable.name));

        return store;
    }
}

