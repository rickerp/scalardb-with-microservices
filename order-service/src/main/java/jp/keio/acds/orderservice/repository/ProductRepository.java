package jp.keio.acds.orderservice.repository;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.CrudException;
import com.scalar.db.io.Key;
import jp.keio.acds.orderservice.dto.CreateProductDto;
import jp.keio.acds.orderservice.dto.GetProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository extends BaseRepository<GetProductDto> {

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
        return get(tx, productId);
    }

    public List<GetProductDto> listProducts(DistributedTransaction tx) throws CrudException {
        return scan(tx);
    }

    @Override
    protected String getNamespace() {
        return NAMESPACE;
    }

    @Override
    protected String getTable() {
        return TABLE_NAME;
    }

    @Override
    protected String getPartitionKey() {
        return PRODUCT_ID;
    }

    @Override
    protected GetProductDto toDto(Result result) {
        return GetProductDto.builder()
                .productId(result.getText(PRODUCT_ID))
                .ownerId(result.getText(OWNER_ID))
                .name(result.getText(NAME))
                .price(result.getDouble(PRICE))
                .build();
    }
}
