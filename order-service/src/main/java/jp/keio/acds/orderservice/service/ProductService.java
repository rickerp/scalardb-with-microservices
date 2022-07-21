package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import jp.keio.acds.orderservice.dto.CreateProductDto;
import jp.keio.acds.orderservice.dto.GetProductDto;
import jp.keio.acds.orderservice.exception.InternalServerErrorException;
import jp.keio.acds.orderservice.exception.NotFoundException;
import jp.keio.acds.orderservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService extends BaseService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          DistributedTransactionManager manager,
                          TwoPhaseCommitTransactionManager microserviceManager) {
        super(manager, microserviceManager);
        this.productRepository = productRepository;
    }

    public String createProduct(CreateProductDto createProductDto) throws InterruptedException {
        return execute((Transaction<String>) tx -> {
            String productId = productRepository.createProduct(tx, createProductDto);
            tx.commit();
            return productId;
        }, null);
    }

    public GetProductDto getProduct(String productId) throws InterruptedException {
        return execute((Transaction<GetProductDto>) tx -> {
            GetProductDto getProductDto = productRepository.getProduct(tx, productId);
            tx.commit();
            return getProductDto;
        }, null);
    }

    public List<GetProductDto> listProducts() throws InterruptedException {
        return execute((Transaction<List<GetProductDto>>) tx -> {
            List<GetProductDto> getProductDtoList = productRepository.listProducts(tx);
            tx.commit();
            return getProductDtoList;
        }, null);
    }
}
