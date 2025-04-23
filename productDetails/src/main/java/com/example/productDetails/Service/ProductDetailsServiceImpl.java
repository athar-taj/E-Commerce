package com.example.productDetails.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.example.productDetails.Model.Product;
import com.example.productDetails.Model.Response.CommonResponse;
import com.example.productDetails.Model.ProductDetails;
import com.example.productDetails.Model.Request.ProductDetailsRequest;
import com.example.productDetails.Repository.ProductDetailsRepository;
import com.example.productDetails.Repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ElasticsearchClient elasticsearchClient;

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


    public ResponseEntity<CommonResponse> saveProductInElastic(ProductDetailsRequest request) {

        Product product = new Product();
        product.setProduct(request.getProduct());
        product.setProductId(request.getProductId());
        product.setSubDetails(request.getSubDetails());

        productRepository.save(product);

        return ResponseEntity.ok(new CommonResponse(200, "Product Details Saved !!", true));
    }

    public ResponseEntity<CommonResponse> getProductByIdInElastic(String id) throws IOException {
        Optional<Product> product = productRepository.findById(id);

        // Synchronous blocking client
        if (elasticsearchClient.exists(p -> p.index("products").id(id)).value()){
            System.out.println("Product Exists");
        }
        else {
            System.out.println("Not Exists");
        }

        if (product.isPresent()) {
            return ResponseEntity.ok(new CommonResponse(200, "Product Details Fetched !!", product.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not Found !!", null));
        }
    }

    public ResponseEntity<CommonResponse> searchProducts(String keyword) throws IOException {
        SearchResponse<JsonData> response = elasticsearchClient.search(s -> s
                        .index("products")
                        .query(q -> q
                                .queryString(qs -> qs
                                        .query(keyword)
                                )
                        ),
                JsonData.class
        );

        List<Map<String, Object>> results = new ArrayList<>();

        for (Hit<JsonData> hit : response.hits().hits()) {
            JsonNode jsonNode = hit.source().to(JsonNode.class);
            Map<String, Object> map = new ObjectMapper().convertValue(jsonNode, Map.class);
            results.add(map);
        }

        return ResponseEntity.ok(new CommonResponse(200, "Product Fetched !!", results));

    }

}
