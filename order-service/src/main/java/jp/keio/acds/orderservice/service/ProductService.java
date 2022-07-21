package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransactionManager;
import jp.keio.acds.orderservice.dto.CreateProductDto;
import jp.keio.acds.orderservice.dto.GetProductDto;
import jp.keio.acds.orderservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService extends BaseService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          DistributedTransactionManager manager) {
        super(manager);
        this.productRepository = productRepository;
    }

    public String createProduct(CreateProductDto createProductDto) throws InterruptedException {
        return execute(tx -> {
            String productId = productRepository.createProduct(tx, createProductDto);
            tx.commit();
            return productId;
        });
    }

    public GetProductDto getProduct(String productId) throws InterruptedException {
        return execute(tx -> {
            GetProductDto getProductDto = productRepository.getProduct(tx, productId);
            tx.commit();
            return getProductDto;
        });
    }

    public List<GetProductDto> listProducts() throws InterruptedException {
        return execute(tx -> {
            List<GetProductDto> getProductDtoList = productRepository.listProducts(tx);
            tx.commit();
            return getProductDtoList;
        });
    }
}
