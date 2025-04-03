package com.example.product.Consumer;

import com.example.product.Model.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
    public void ProductConsumer(Product product){
        System.out.println("Consume JSON -> " + product.toString());
    }
}
