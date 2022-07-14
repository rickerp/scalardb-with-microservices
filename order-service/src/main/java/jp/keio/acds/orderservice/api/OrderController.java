package jp.keio.acds.orderservice.api;

import jp.keio.acds.orderservice.dto.OrderDto;
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
    public String createOrder(@RequestBody OrderDto orderDto) throws Exception {
        return orderService.createOrder(orderDto);
    }

    @GetMapping()
    public List<OrderDto> listOrders() {
        return this.orderService.listOrders();
    }

    @GetMapping("/{order_id}")
    public OrderDto getOrder(@PathVariable("order_id") int orderId) {
        return this.orderService.getOrder(orderId);
    }
}
