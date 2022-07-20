package jp.keio.acds.orderservice.api;

import jp.keio.acds.orderservice.dto.CreateOrderDto;
import jp.keio.acds.orderservice.dto.GetOrderDto;
import jp.keio.acds.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody CreateOrderDto createOrderDto) throws InterruptedException {
        return orderService.createOrder(createOrderDto);
    }

    @GetMapping()
    public List<GetOrderDto> listOrders() {
        return this.orderService.listOrders();
    }

    @GetMapping("/{order_id}")
    public GetOrderDto getOrder(@PathVariable("order_id") String orderId) throws InterruptedException {
        return this.orderService.getOrder(orderId);
    }
}
