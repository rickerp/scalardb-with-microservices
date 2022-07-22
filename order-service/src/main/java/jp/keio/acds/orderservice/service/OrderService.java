package jp.keio.acds.orderservice.service;

import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.TwoPhaseCommitTransaction;
import com.scalar.db.api.TwoPhaseCommitTransactionManager;
import com.scalar.db.exception.transaction.*;
import jp.keio.acds.orderservice.dto.*;
import jp.keio.acds.orderservice.exception.BadRequestException;
import jp.keio.acds.orderservice.repository.OrderProductRepository;
import jp.keio.acds.orderservice.repository.OrderRepository;
import jp.keio.acds.orderservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService extends BaseService {

    private static final String STORE_EXISTS_URI = "/stores/./checkUser";

    private final OrderRepository orderRepository;

    private final OrderProductRepository orderProductRepository;

    private final ProductRepository productRepository;

    private final WebClient userMicroserviceClient;


    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderProductRepository orderProductRepository,
                        ProductRepository productRepository,
                        DistributedTransactionManager manager,
                        TwoPhaseCommitTransactionManager microserviceManager,
                        WebClient userMicroserviceClient) {
        super(manager, microserviceManager);
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
        this.userMicroserviceClient = userMicroserviceClient;
    }

    public String createOrder(CreateOrderDto createOrderDto) throws InterruptedException {
        return execute(tx -> {
            String txId = tx.getId();

            sendJoinRequest(txId, userMicroserviceClient);

            List<GetProductDto> products = checkProducts(tx, createOrderDto);
            String ownerId = checkProductsOwner(products);

            String orderId = orderRepository.createOrder(tx, ownerId, createOrderDto.getToId());

            for (OrderProductDto orderProductDto : createOrderDto.getOrderProducts()) {
                orderProductRepository.createOrderProduct(tx, orderId,
                        orderProductDto.getProductId(),
                        orderProductDto.getCount());
            }

            sendStoreExistsRequest(txId, createOrderDto.getToId());

            prepareTransaction(tx, userMicroserviceClient);
            commitTransaction(tx, userMicroserviceClient);

            return orderId;
        }, userMicroserviceClient);
    }

    public GetOrderDto getOrder(String orderId) throws InterruptedException {
        return execute((Transaction<GetOrderDto>) tx -> {
            GetOrderDto order = orderRepository.getOrder(tx, orderId);
            tx.commit();
            return order;
        }, null);
    }

    public List<GetOrderDto> listOrders() throws InterruptedException {
        return execute((Transaction<List<GetOrderDto>>) tx -> {
            List<GetOrderDto> getOrderDtoList = orderRepository.listOrders(tx);
            tx.commit();
            return getOrderDtoList;
        }, null);
    }

    private List<GetProductDto> checkProducts(TwoPhaseCommitTransaction tx, CreateOrderDto createOrderDto) throws CrudException {
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

    private void sendStoreExistsRequest(String txId, String storeId) {
        userMicroserviceClient.put()
                .uri(STORE_EXISTS_URI.replace(".", storeId))
                .body(BodyInserters.fromValue(RegisterOrderDto.builder().txId(txId).build()))
                .retrieve()
                .bodyToMono(Void.class)
                .block(Duration.ofSeconds(5L));
    }
}
