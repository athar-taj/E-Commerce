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


    @Value("${rabbitmq.suggested.category.queue.name}")
    private String suggestedCategoryQueue;

    @Value("${rabbitmq.suggested.category.queue.exchange}")
    private String suggestedCategoryExchange;

    @Value("${rabbitmq.suggested.category.routing.key}")
    private String suggestedCategoryRoutingKey;


    @Bean
    public Queue suggestedCategoryQueue(){
        return new Queue(suggestedCategoryQueue,true);
    }
    @Bean
    public DirectExchange suggestedCategoryExchange(){
        return new DirectExchange(suggestedCategoryExchange,true,false);
    }
    @Bean
    public Binding suggestedCategoryBinding(){
        return BindingBuilder.bind(suggestedCategoryQueue())
                .to(suggestedCategoryExchange())
                .with(suggestedCategoryRoutingKey);
    }


    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
