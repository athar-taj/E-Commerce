package com.example.product.Consumer;

import com.example.product.Model.Product;
import com.example.product.Model.Request.StockRequest;
import com.example.product.Repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

//    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
//    public void productConsumer(Product product){
//        System.out.println("Consume JSON -> " + product.toString());
//    }


    @Autowired
    ProductRepository productRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.stock.queue.name}")
    public Boolean checkStock(StockRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product != null && product.getStock() >= request.getQuantity()) {
            return true;
        } else {
            return false;
        }
    }

    @RabbitListener(queues = "${rabbitmq.stock.reduce.queue.name}")
    public void reduceStock(StockRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product != null && product.getStock() >= request.getQuantity()) {
            product.setStock(product.getStock() - request.getQuantity());
            productRepository.save(product);
            System.out.println("Stock reduced for: " + product.getName());
        } else {
            System.out.println("Insufficient stock ");
        }
    }

    @RabbitListener(queues = "${rabbitmq.product.queue.name}")
    public Boolean isProductAvailable(long productId) {
        return productRepository.existsById(productId);
    }

    @RabbitListener(queues = "${rabbitmq.product.available.queue.name}")
    public Boolean isProductAvailableForCart(long productId) {
        return productRepository.existsById(productId);
    }
}
