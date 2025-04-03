package com.example.product.Publisher;

import com.example.product.Model.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ProductProducer {

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

    public void sendProductBody(Product product){
        System.out.println("Product From API -> "+product.toString());
        rabbitTemplate.convertAndSend(exchange,jsonRoutingKey,product);
    }
}
