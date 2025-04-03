package com.example.payment.Consumer;

import com.example.payment.Model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private Order latestOrder;

    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"})
    public void consumeOrderFromQueue(Order order) {
        System.out.println("Consumed JSON from Queue -> " + order.toString());
        latestOrder = order;
    }

    @RabbitListener(queues = {"${rabbitmq.vip.queue.name}"})
    public void consumeVipOrder(Order order) {
        System.out.println("Consumed VIP Order from Queue -> " + order.toString());
        latestOrder = order;
    }

    @RabbitListener(queues = {"${rabbitmq.regular.queue.name}"})
    public void consumeRegularOrder(Order order) {
        System.out.println("Consumed Regular Order from Queue -> " + order.toString());
        latestOrder = order;
    }

    public Order getLatestOrder() {
        return latestOrder;
    }

    public void setLatestOrder(Order order){
        latestOrder = order;
    }
}