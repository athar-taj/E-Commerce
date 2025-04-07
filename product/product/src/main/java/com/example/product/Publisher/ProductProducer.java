package com.example.product.Publisher;

import com.example.product.Model.Product;
import com.example.product.Repository.ProductRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ProductProducer {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public void sendProductParam(String message){
        System.out.println("Message From API -> "+message);
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
    }

    @Value("${rabbitmq.json.routing.key}")
    private String jsonRoutingKey;

    public void sendProductBody(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        rabbitTemplate.convertAndSend(exchange,jsonRoutingKey,product.get());
        System.out.println("Product From API -> "+product.get());
    }
}
