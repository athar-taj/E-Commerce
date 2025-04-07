package com.example.rating.Producer;

import com.example.rating.Model.Rating;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RatingProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public void sendRatingBody(Rating rating){
        System.out.println("Rating From API -> "+ rating);
        rabbitTemplate.convertAndSend(exchange,routingKey,rating);
    }
}
