package com.example.product.Consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryConsumer {

    @RabbitListener(queues = "${rabbitmq.suggested.category.queue.name}")
    public void getSuggestedCategory(List<String> list){
        System.out.println(list);
    }
}
