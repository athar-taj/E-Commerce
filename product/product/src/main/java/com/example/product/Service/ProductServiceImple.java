package com.example.product.Service;

import com.example.product.Model.Product;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImple implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<CommonResponse> createProduct(Product product) {
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Product added successfully !!", true));
    }

    public ResponseEntity<CommonResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Products List !!", products));
    }

    public ResponseEntity<CommonResponse> getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Products List !!", product));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Products Not Found !!", null));
        }
    }

    public ResponseEntity<CommonResponse> updateProduct(Long id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product1 = existingProduct.get();

            product1.setName(product.getName());
            product1.setPrice(product.getPrice());
            product1.setStock(product.getStock());
            product1.setDescription(product.getDescription());
            productRepository.save(product1);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Products Updated !!", product1));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Products Not Found !!", null));
        }
    }

    public ResponseEntity<CommonResponse> deleteProduct(Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Products Removed !!", null));
    }

    public boolean checkProductStock(long productId,int quantity) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.get().getId() == productId && product.get().getStock() >= quantity) {
            return true;
        }
        return false;
    }

    public ResponseEntity<CommonResponse> updateProductStock(long productId, int quantity) {
        Optional<Product> existingProduct = productRepository.findById(productId);

        if(existingProduct.get().getStock() > 0){
            existingProduct.get().setStock(existingProduct.get().getStock() - quantity);
            productRepository.save(existingProduct.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Products Stock Updated !!", existingProduct.get()));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(HttpStatus.BAD_REQUEST.value(), "Products Stock is not Available !!", null));
        }
    }
}
