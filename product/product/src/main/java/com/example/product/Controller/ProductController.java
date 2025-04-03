package com.example.product.Controller;

import com.example.product.Consumer.ProductConsumer;
import com.example.product.Model.Product;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Publisher.ProductProducer;
import com.example.product.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductProducer productProducer;
    @Autowired
    private ProductConsumer productConsumer;


    @GetMapping("/publish")
    public ResponseEntity<String> sendProduct(@RequestParam("message") String msg){
        productProducer.sendProductParam(msg);
        return ResponseEntity.ok("Message sent to RabbitMQ...");
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendProductBody(@RequestBody Product product){
        productProducer.sendProductBody(product);

        return ResponseEntity.ok("Product sent to RabbitMQ...");
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
    @GetMapping
    public ResponseEntity<CommonResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/check-stock/{productId}/{quantity}")
    public Boolean checkProductStock(@PathVariable Long productId, @PathVariable int quantity) {
        return productService.checkProductStock(productId, quantity);
    }

    @PatchMapping("/update-stock/{productId}/{quantity}")
    public ResponseEntity<CommonResponse> updateProductStock(@PathVariable Long productId, @PathVariable int quantity) {
        return productService.updateProductStock(productId, quantity);
    }
}

