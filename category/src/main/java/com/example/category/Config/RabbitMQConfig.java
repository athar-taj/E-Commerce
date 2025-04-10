package com.example.category.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.category.queue.name}")
    private String categoryQueue;

    @Value("${rabbitmq.category.queue.exchange}")
    private String categoryExchange;

    @Value("${rabbitmq.category.routing.key}")
    private String categoryRoutingKey;


    @Bean
    public Queue categoryQueue(){
        return new Queue(categoryQueue,true);
    }
    @Bean
    public DirectExchange categoryExchange(){
        return new DirectExchange(categoryExchange,true,false);
    }
    @Bean
    public Binding categoryBinding(){
        return BindingBuilder.bind(categoryQueue())
                .to(categoryExchange())
                .with(categoryRoutingKey);
    }

}
