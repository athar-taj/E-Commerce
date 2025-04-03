package com.example.product.Service;

import com.example.product.Model.Product;
import com.example.product.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ResponseEntity<CommonResponse> createProduct(Product product);
    ResponseEntity<CommonResponse> getAllProducts();
    ResponseEntity<CommonResponse> getProductById(Long id);
    ResponseEntity<CommonResponse> updateProduct(Long id, Product updatedProduct);
    ResponseEntity<CommonResponse> deleteProduct(Long id);
    boolean checkProductStock(long productId,int quantity);
    ResponseEntity<CommonResponse> updateProductStock(long productId,int quantity);
}
