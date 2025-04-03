package com.example.order.Consumer;

import com.example.order.Model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

//    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
//    public void OrderConsumer(Order order){
//        System.out.println("Consume JSON -> " + order.toString());
//    }
}
