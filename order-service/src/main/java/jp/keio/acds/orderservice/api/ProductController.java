package jp.keio.acds.orderservice.api;

import jp.keio.acds.orderservice.dto.CreateProductDto;
import jp.keio.acds.orderservice.dto.GetOrderDto;
import jp.keio.acds.orderservice.dto.GetProductDto;
import jp.keio.acds.orderservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody CreateProductDto createProductDto) throws InterruptedException {
        return productService.createProduct(createProductDto);
    }

    @GetMapping("/{product_id}")
    public GetProductDto getProduct(@PathVariable("product_id") String productId) throws InterruptedException {
        return this.productService.getProduct(productId);
    }
}
