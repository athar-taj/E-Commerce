package com.example.product.ServiceImple;

import com.example.product.Consumer.ProductConsumer;
import com.example.product.Model.Product;
import com.example.product.Model.Request.ProductRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Model.Response.ProductResponse;
import com.example.product.Repository.ProductRepository;
import com.example.product.Service.OtherImpl.FileStorage;
import com.example.product.Service.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ProductConsumer productConsumer;
    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<CommonResponse> addProduct(ProductRequest productRequest, MultipartFile file) {
        try {
            Product product = new Product();

            if(productRepository.existsByNameIgnoreCase(productRequest.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CommonResponse(409, "Product Name is already Taken !!", null));
            }

            Boolean response = (Boolean) rabbitTemplate.convertSendAndReceive("category_exchange","category_routing_key",productRequest.getCategory());
            System.out.println(response);
            if(Boolean.FALSE.equals(response)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse(404, "Category not found: " + productRequest.getCategory(), null));
            }

            product.setCategory(productRequest.getCategory());
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());
            product.setBrand(productRequest.getBrand());
            product.setSpecifications(productRequest.getSpecifications());
            product.setAbout(productRequest.getAbout());
            product.setProductRating(0);
            product.setTotalRatings(0);
            product.setImageUrl(FileStorage.saveFile(file,"product"));
            product.setDiscount(productRequest.getDiscount());
            product.setCreatedAt(LocalDateTime.now());
            product.setActive(Boolean.TRUE);
            product.setIsVariant(productRequest.getVariant());

            productRepository.save(product);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CommonResponse(201, "Product added successfully !!", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse(500, "Error while adding product: " + e.getMessage(), false));
        }
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

    public ResponseEntity<CommonResponse> updateProduct(Long id, ProductRequest productRequest, MultipartFile file) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not Found !!", null));
        }

        Product existingProduct = optionalProduct.get();

        if (!existingProduct.getName().equalsIgnoreCase(productRequest.getName()) &&
                productRepository.existsByNameIgnoreCase(productRequest.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResponse(409, "Product name is already taken by another product !!", null));
        }

        existingProduct.setName(productRequest.getName());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());

//        if (!categoryRepository.existsByName(productRequest.getCategory())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new CommonResponse(404, "Category Not Found !!", null));
//        }

        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setBrand(productRequest.getBrand());
        existingProduct.setSpecifications(productRequest.getSpecifications());
        existingProduct.setAbout(productRequest.getAbout());
        existingProduct.setDiscount(productRequest.getDiscount());
        if (file != null && !file.isEmpty()) {
            String imageUrl = FileStorage.saveFile(file, "product");
            existingProduct.setImageUrl(imageUrl);
        }

        productRepository.save(existingProduct);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Product Updated Successfully !!", existingProduct));
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

    public boolean isProductAvailable(long productId){
        return productRepository.existsById(productId);
    }

    public ResponseEntity<CommonResponse> getProductsByMaxDiscount(int discount) {
        List<Product> products = productRepository.findByDiscountProduct(discount);

        List<ProductResponse> responseList = new ArrayList<>();
        for (Product product : products) {
            ProductResponse res = new ProductResponse();
            res.setName(product.getName());
            res.setPrice(product.getPrice());
            res.setDiscount(product.getDiscount());
            res.setImageUrl(product.getImageUrl());
            res.setRating(product.getTotalRatings());

            int discountedPrice = (int) product.getPrice() - (product.getPrice() * product.getDiscount() / 100);
            res.setDiscount_price(discountedPrice);
            responseList.add(res);
        }

        return ResponseEntity.ok(new CommonResponse(200, "Products with discount Up to " + discount + "% fetched", responseList));
    }

    public ResponseEntity<CommonResponse> suggestedProduct(Long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Products Not Found !!", null));
        }

        rabbitTemplate.convertAndSend("product_suggestion_exchange","suggestion_routing_key",product.get().getCategory());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Category Sent !!", null));

    }
}
