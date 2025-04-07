package com.example.productDetails.Consumer;

import com.example.productDetails.Model.ProductDetails;
import com.example.productDetails.Model.Request.ProductIdRequest;
import com.example.productDetails.Repository.ProductDetailsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductDetailsConsumer {

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @RabbitListener(queues = "${rabbitmq.product.details.queue.name}")
    public ProductDetails productDetails(ProductIdRequest productIdRequest) {
        System.out.println("Come at here");
        Optional<ProductDetails> productDetails =  productDetailsRepository.findByProductId(productIdRequest.getProductId());
        return productDetails.get();
    }
}