package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.*;
import jp.keio.acds.orderservice.dto.*;
import jp.keio.acds.orderservice.exception.NotFoundException;
import jp.keio.acds.orderservice.exception.InternalServerErrorException;
import jp.keio.acds.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderService extends BaseService {

    private static final int MAX_TRANSACTION_RETRIES = 3;

    private final OrderRepository orderRepository;

    private final ProductService productService;

    // For two-phase commit transactions
    //private final TwoPhaseCommitTransactionManager twoPhaseCommitTransactionManager;


    @Autowired
    public OrderService(OrderRepository orderRepository,
                        DistributedTransactionManager manager,
                        ProductService productService) {
        super(manager);
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public String createOrder(CreateOrderDto createOrderDto) throws InterruptedException {
        return execute(tx -> {
            String orderId = orderRepository.createOrder(tx, createOrderDto);
            tx.commit();
            return orderId;
        });
    }

    public GetOrderDto getOrder(String orderId) throws InterruptedException {
        return execute(tx -> {
            GetOrderDto order = orderRepository.getOrder(tx, orderId);
            tx.commit();
            return order;
        });
    }

    public List<GetOrderDto> listOrders() throws InterruptedException {
        return execute(tx -> {
            List<GetOrderDto> getOrderDtoList = orderRepository.listOrders(tx);
            tx.commit();
            return getOrderDtoList;
        });
    }
}
