package com.example.productDetails.Service;

import com.example.productDetails.Model.CommonResponse;
import com.example.productDetails.Model.ProductDetails;
import com.example.productDetails.Model.Request.ProductDetailsRequest;
import com.example.productDetails.Model.Request.ProductSubDetailRequest;
import com.example.productDetails.Repository.ProductDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    public ResponseEntity<CommonResponse> saveProduct(ProductDetailsRequest request) {
        if (productDetailsRepository.findByProductId(request.getProductId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResponse(409, "Product Already Available !!", false));
        }

        ProductDetails product = new ProductDetails();
        product.setProductId(request.getProductId());
        product.setSubDetails(request.getSubDetails());

        productDetailsRepository.save(product);

        return ResponseEntity.ok(new CommonResponse(200, "Product Details Saved !!", true));
    }

    public ResponseEntity<CommonResponse> getProductById(String id) {
        Optional<ProductDetails> productDetails = productDetailsRepository.findById(id);
        if(productDetails.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(200,"Product Details Fetched !! ",productDetails));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse(404,"Product Not Found !! ",null));
        }
    }

    public ResponseEntity<CommonResponse> updateProduct(long productId, ProductDetailsRequest request) {
        Optional<ProductDetails> existing = productDetailsRepository.findByProductId(productId);

        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not Found !!", false));
        }

        ProductDetails product = existing.get();
        product.setSubDetails(request.getSubDetails());
        productDetailsRepository.save(product);

        return ResponseEntity.ok(new CommonResponse(200, "Product Details Updated Successfully", true));
    }
}
