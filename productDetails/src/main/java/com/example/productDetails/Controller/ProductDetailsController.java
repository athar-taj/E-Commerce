package com.example.productDetails.Controller;

import com.example.productDetails.Model.Response.CommonResponse;
import com.example.productDetails.Model.Request.ProductDetailsRequest;
import com.example.productDetails.Service.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/products/details")
public class ProductDetailsController {

    @Autowired
    private ProductDetailsService productDetailsService;

    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addProductDetails(@RequestBody ProductDetailsRequest request) {
        return productDetailsService.saveProduct(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getProductDetails(@PathVariable String id) {
        return productDetailsService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateProductDetails(@PathVariable Long productId,@RequestBody ProductDetailsRequest request){
        return productDetailsService.updateProduct(productId,request);
    }


    @PostMapping("/elastic/add")
    public ResponseEntity<CommonResponse> addProductDetailsInElastic(@RequestBody ProductDetailsRequest request) {
        return productDetailsService.saveProductInElastic(request);
    }

    @GetMapping("/elastic/{id}")
    public ResponseEntity<CommonResponse> getProductDetailsInElastic(@PathVariable String id) throws IOException {
        return productDetailsService.getProductByIdInElastic(id);
    }

    @GetMapping("/elastic/keyword/{keyword}")
    public ResponseEntity<CommonResponse> searchProducts(@PathVariable String keyword) throws IOException {
        return productDetailsService.searchProducts(keyword);
    }
}

