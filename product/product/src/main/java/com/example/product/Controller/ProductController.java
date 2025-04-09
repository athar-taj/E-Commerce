package com.example.product.Controller;

import com.example.product.Consumer.ProductConsumer;import com.example.product.Model.Request.ProductRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Publisher.ProductProducer;
import com.example.product.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductProducer productProducer;
    @Autowired
    private ProductConsumer productConsumer;
    @Autowired
    Validator validator;

    @GetMapping("/publish")
    public ResponseEntity<String> sendProduct(@RequestParam("message") String msg){
        productProducer.sendProductParam(msg);
        return ResponseEntity.ok("Message sent to RabbitMQ...");
    }

    @GetMapping("/publish/{productId}")
    public ResponseEntity<String> sendProductBody(@PathVariable Long productId){
        productProducer.sendProductBody(productId);
        return ResponseEntity.ok("Product sent to RabbitMQ...");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> createProduct(@RequestPart("product") @Valid String ProductRequestJson,
                                                        @RequestParam("image") MultipartFile image) throws IOException, ConstraintViolationException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest productRequest = objectMapper.readValue(ProductRequestJson, ProductRequest.class);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return productService.addProduct(productRequest,image);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> updateProduct(@PathVariable Long id,
                                                        @RequestPart("product") @Valid String productRequestJson,
                                                        @RequestParam(value = "image", required = false) MultipartFile image)
            throws IOException, ConstraintViolationException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest productRequest = objectMapper.readValue(productRequestJson, ProductRequest.class);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return productService.updateProduct(id, productRequest, image);
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

    @GetMapping("/available/{productId}")
    public Boolean isProductAvailable(@PathVariable Long productId){
        return productService.isProductAvailable(productId);
    }

    @GetMapping("/discount-upto/{discount}")
    public ResponseEntity<CommonResponse> getProductDiscountUpto(@PathVariable int discount){
        return productService.getProductsByMaxDiscount(discount);
    }

    @GetMapping("/suggest/{productId}")
    public ResponseEntity<CommonResponse> getSuggestionProduct(@PathVariable Long productId){
        return productService.suggestedProduct(productId);
    }
}

