package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.*;
import jp.keio.acds.orderservice.dto.CreateProductDto;
import jp.keio.acds.orderservice.dto.GetOrderDto;
import jp.keio.acds.orderservice.dto.GetProductDto;
import jp.keio.acds.orderservice.exception.OrderNotFoundException;
import jp.keio.acds.orderservice.exception.ServiceException;
import jp.keio.acds.orderservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProductService {

    private static final int MAX_TRANSACTION_RETRIES = 3;

    private final ProductRepository productRepository;

    private final DistributedTransactionManager manager;


    @Autowired
    public ProductService(ProductRepository productRepository,
                          DistributedTransactionManager manager) {
        this.productRepository = productRepository;
        this.manager = manager;
    }

    public String createProduct(CreateProductDto createProductDto) throws InterruptedException {
        int retryCount = 0;

        while (true) {
            DistributedTransaction tx = startTransaction();

            try {
                String productId = productRepository.createProduct(tx, createProductDto);
                tx.commit();
                return productId;
            } catch (CommitConflictException | CrudConflictException e) {
                retryTransaction(++retryCount);
            } catch (CommitException | CrudException | UnknownTransactionStatusException e) {
                throw new ServiceException("ERROR : Failed to create product", e);
            } finally {
                abortTransaction(tx);
            }
        }
    }

    public GetProductDto getProduct(String productId) throws InterruptedException {
        int retryCount = 0;

        while (true) {
            DistributedTransaction tx = startTransaction();

            try {
                GetProductDto getProductDto = productRepository.getProduct(tx, productId);
                tx.commit();
                return getProductDto;
            } catch (CommitConflictException | CrudConflictException e) {
                retryTransaction(++retryCount);
            } catch (CommitException | CrudException | UnknownTransactionStatusException e) {
                throw new ServiceException("ERROR : Failed to get product", e);
            } catch (OrderNotFoundException e) {
                throw new ServiceException(e.getMessage(), e);
            } finally {
                abortTransaction(tx);
            }
        }
    }

    private DistributedTransaction startTransaction() {
        try {
            return manager.start();
        } catch (TransactionException e) {
            throw new ServiceException("ERROR : Could not start transaction manager", e);
        }
    }

    private void retryTransaction(int retryCount) throws InterruptedException {
        if (retryCount == MAX_TRANSACTION_RETRIES) {
            throw new ServiceException("ERROR : Failed transaction after 3 retries");
        }
        TimeUnit.MILLISECONDS.sleep(100);
    }

    private void abortTransaction(DistributedTransaction tx) {
        try {
            tx.abort();
        } catch (AbortException ex) {
            log.error(ex.getMessage(), ex);
            throw new ServiceException("ERROR : Could not abort transaction");
        }
    }
}
