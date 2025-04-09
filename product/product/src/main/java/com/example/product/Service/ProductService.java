package com.example.product.Service;

import com.example.product.Model.Request.ProductRequest;
import com.example.product.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ResponseEntity<CommonResponse> addProduct(ProductRequest productRequest, MultipartFile file) throws IOException;
    ResponseEntity<CommonResponse> getAllProducts();
    ResponseEntity<CommonResponse> getProductById(Long id);
    ResponseEntity<CommonResponse> updateProduct(Long id, ProductRequest productRequest,MultipartFile file)throws IOException;
    ResponseEntity<CommonResponse> deleteProduct(Long id);
    boolean checkProductStock(long productId,int quantity);
    ResponseEntity<CommonResponse> updateProductStock(long productId,int quantity);
    boolean isProductAvailable(long productId);
    ResponseEntity<CommonResponse> getProductsByMaxDiscount(int discount);
    ResponseEntity<CommonResponse> suggestedProduct(Long id);
}
