package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.*;
import jp.keio.acds.orderservice.dto.OrderDto;
import jp.keio.acds.orderservice.exception.ServiceException;
import jp.keio.acds.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderService {

    private static final int MAX_TRANSACTION_RETRIES = 3;

    private final OrderRepository orderRepository;

    // For normal transactions
    private final DistributedTransactionManager manager;

    // For two-phase commit transactions
    //private final TwoPhaseCommitTransactionManager twoPhaseCommitTransactionManager;


    @Autowired
    public OrderService(OrderRepository orderRepository,
                        DistributedTransactionManager manager) {
        this.orderRepository = orderRepository;
        this.manager = manager;
    }

    public String createOrder(OrderDto orderDto) throws InterruptedException {
        int retryCount = 0;

        while (true) {
            DistributedTransaction tx = startTransaction();

            try {
                String orderId = orderRepository.createOrder(tx, orderDto);
                tx.commit();
                return orderId;
            } catch (CommitConflictException | CrudConflictException e) {
                retryTransaction(++retryCount);
            //} catch (CommitException | CrudException | UnknownTransactionStatusException e) {
                //throw new ServiceException("ERROR : Failed to create order", e);
            } catch (CommitException e) {
                throw new ServiceException("ERROR : Failed to create order (A)", e);
            } catch (CrudException e) {
                throw new ServiceException("ERROR : Failed to create order (B)", e);
            } catch (UnknownTransactionStatusException e) {
                throw new ServiceException("ERROR : Failed to create order (C)", e);
            } finally {
                abortTransaction(tx);
            }
        }
    }

    public List<OrderDto> listOrders() {
        return null;
    }

    public OrderDto getOrder(int orderId) {
        return null;
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
