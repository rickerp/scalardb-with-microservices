package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.exception.transaction.*;
import jp.keio.acds.orderservice.dto.*;
import jp.keio.acds.orderservice.exception.BadRequestException;
import jp.keio.acds.orderservice.repository.OrderProductRepository;
import jp.keio.acds.orderservice.repository.OrderRepository;
import jp.keio.acds.orderservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService extends BaseService {

    private final OrderRepository orderRepository;

    private final OrderProductRepository orderProductRepository;

    private final ProductRepository productRepository;

    // For two-phase commit transactions
    //private final TwoPhaseCommitTransactionManager twoPhaseCommitTransactionManager;


    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderProductRepository orderProductRepository,
                        ProductRepository productRepository,
                        DistributedTransactionManager manager) {
        super(manager);
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }

    public String createOrder(CreateOrderDto createOrderDto) throws InterruptedException {
        return execute(tx -> {

            // TODO connect to user MS and validate to_id

            List<GetProductDto> products = checkProducts(tx, createOrderDto);
            String ownerId = checkProductsOwner(products);

            String orderId = orderRepository.createOrder(tx, ownerId, createOrderDto.getToId());

            for (OrderProductDto orderProductDto : createOrderDto.getOrderProducts()) {
                orderProductRepository.createOrderProduct(tx, orderId,
                        orderProductDto.getProductId(),
                        orderProductDto.getCount());
            }

            tx.commit();
            return orderId;
        }, null);
    }

    public GetOrderDto getOrder(String orderId) throws InterruptedException {
        return execute(tx -> {
            GetOrderDto order = orderRepository.getOrder(tx, orderId);
            tx.commit();
            return order;
        }, null);
    }

    public List<GetOrderDto> listOrders() throws InterruptedException {
        return execute(tx -> {
            List<GetOrderDto> getOrderDtoList = orderRepository.listOrders(tx);
            tx.commit();
            return getOrderDtoList;
        }, null);
    }

    private List<GetProductDto> checkProducts(DistributedTransaction tx, CreateOrderDto createOrderDto) throws CrudException {
        List<String> productIds = createOrderDto.getOrderProducts().stream()
                .map(OrderProductDto::getProductId)
                .collect(Collectors.toList());

        List<GetProductDto> products = new ArrayList<>();
        for (String productId : productIds) {
            products.add(productRepository.getProduct(tx, productId));
        }

        return products;
    }

    private String checkProductsOwner(List<GetProductDto> products) {
        List<String> ownerIds = products.stream()
                .map(GetProductDto::getOwnerId)
                .distinct()
                .collect(Collectors.toList());

        if (ownerIds.size() > 1) {
            throw new BadRequestException();
        }

        return ownerIds.get(0);
    }
}
