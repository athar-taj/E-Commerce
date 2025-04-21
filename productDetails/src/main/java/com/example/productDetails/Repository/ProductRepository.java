package com.example.productDetails.Repository;

import com.example.productDetails.Model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ProductRepository extends ElasticsearchRepository<Product,Integer> {
    Optional<Object> findByProductId(Long productId);
}
