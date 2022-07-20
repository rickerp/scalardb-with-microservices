package jp.keio.acds.orderservice.repository;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.io.Key;
import jp.keio.acds.orderservice.dto.CreateOrderDto;
import jp.keio.acds.orderservice.dto.CreateProductDto;
import jp.keio.acds.orderservice.dto.GetOrderDto;
import jp.keio.acds.orderservice.dto.GetProductDto;
import jp.keio.acds.orderservice.exception.OrderNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class ProductRepository {

    private static final String NAMESPACE = "order-service";
    private static final String TABLE_NAME = "product";
    private static final String PRODUCT_ID = "id";
    private static final String OWNER_ID = "owner_id";
    private static final String NAME = "name";
    private static final String PRICE = "price";


    public String createProduct(DistributedTransaction tx, CreateProductDto createProductDto) throws CrudException {
        String productId = UUID.randomUUID().toString();

        Put put = Put.newBuilder()
                .namespace(NAMESPACE)
                .table(TABLE_NAME)
                .partitionKey(Key.ofText(PRODUCT_ID, productId))
                .textValue(OWNER_ID, createProductDto.getOwnerId())
                .textValue(NAME, createProductDto.getName())
                .doubleValue(PRICE, createProductDto.getPrice())
                .build();

        tx.put(put);
        return productId;
    }

    public GetProductDto getProduct(DistributedTransaction tx, String productId) throws CrudException {
        Get get = Get.newBuilder()
                .namespace(NAMESPACE)
                .table(TABLE_NAME)
                .partitionKey(Key.ofText(PRODUCT_ID, productId))
                .build();

        return toDto(tx.get(get)
                .orElseThrow(
                        () -> new OrderNotFoundException("ERROR : Product with ID " + productId + " does not exist")));
    }

    private GetProductDto toDto(Result result) {
        return GetProductDto.builder()
                .productId(result.getText(PRODUCT_ID))
                .ownerId(result.getText(OWNER_ID))
                .name(result.getText(NAME))
                .price(result.getDouble(PRICE))
                .build();
    }
}
