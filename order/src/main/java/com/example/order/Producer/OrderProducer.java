package com.example.order.Producer;

import com.example.order.Model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public void sendOrderParam(String message) {
        System.out.println("Message From API -> " + message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    @Value("${rabbitmq.json.routing.key}")
    private String jsonRoutingKey;

    public void sendOrderBody(Order order) {
        System.out.println("Product From API -> " + order.toString());
        rabbitTemplate.convertAndSend(exchange, jsonRoutingKey, order);
        System.out.println("uploaded");
    }



    @Autowired
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.headers.exchange}")
    private String headersExchange;

    public void sendOrderHeaderBody(Order order) {
        try {
            rabbitTemplate.convertAndSend(headersExchange, "", order, message -> {
                message.getMessageProperties().setContentType("application/json");
                message.getMessageProperties().setHeader("customerType", order.getRole());
                return message;
            });

            System.out.println("Sent order: " + order.toString());
        } catch (Exception e) {
            System.err.println("Error sending order: " + e.getMessage());
        }
    }


}