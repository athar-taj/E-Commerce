package com.example.product.ServiceImple;

import com.example.product.Consumer.CategoryCheckResponse;
import com.example.product.Consumer.ProductConsumer;
import com.example.product.Model.Product;
import com.example.product.Model.ProductDetails;
import com.example.product.Model.Request.CategoryCheckRequest;
import com.example.product.Model.Request.ProductIdRequest;
import com.example.product.Model.Request.ProductRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Repository.ProductRepository;
import com.example.product.Service.OtherImpl.FileStorage;
import com.example.product.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

            String categoryName = productRequest.getCategory();
            String url = "http://localhost:8088/api/categories/isAvailable/" + categoryName;

            ResponseEntity<Boolean> categoryResponse = restTemplate.getForEntity(url, Boolean.class);
            System.out.println(categoryResponse);
            if (!Boolean.TRUE.equals(categoryResponse.getBody())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse(404, "Category not found: " + categoryName, null));
            }

//            CategoryCheckRequest request = new CategoryCheckRequest();
//            request.setCategoryName(productRequest.getCategory());
//            CategoryCheckResponse response = (CategoryCheckResponse) rabbitTemplate.convertSendAndReceive("category_exchange","category_routing_key",request);
//            System.out.println(response);

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
        ProductIdRequest productIdRequest = new ProductIdRequest();
        productIdRequest.setProductId(id);

        ProductDetails productDetails = (ProductDetails) rabbitTemplate.convertSendAndReceive(
                "product_exchange", "product_details_routing_key", productIdRequest);

        if (product.isPresent()) {
            System.out.println(productDetails);
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

}
