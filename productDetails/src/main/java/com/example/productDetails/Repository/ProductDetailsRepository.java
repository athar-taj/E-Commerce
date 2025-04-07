package com.example.productDetails.Repository;

import com.example.productDetails.Model.ProductDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDetailsRepository extends MongoRepository<ProductDetails,String> {
    Optional<ProductDetails> findByProductId(long productId);
}
