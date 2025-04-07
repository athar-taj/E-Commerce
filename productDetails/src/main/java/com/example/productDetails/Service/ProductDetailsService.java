package com.example.productDetails.Service;

import com.example.productDetails.Model.CommonResponse;
import com.example.productDetails.Model.Request.ProductDetailsRequest;
import org.springframework.http.ResponseEntity;

public interface ProductDetailsService {
    ResponseEntity<CommonResponse> saveProduct(ProductDetailsRequest request);
    ResponseEntity<CommonResponse> getProductById(String id);
    ResponseEntity<CommonResponse> updateProduct(long productId, ProductDetailsRequest request);
}
