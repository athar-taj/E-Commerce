package com.example.cart.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.product.available.queue.name}")
    private String productQueue;

    @Value("${rabbitmq.product.available.queue.exchange}")
    private String productExchange;

    @Value("${rabbitmq.product.available.routing.key}")
    private String productRoutingKey;


    @Bean
    public Queue productQueue(){
        return new Queue(productQueue,true);
    }
    @Bean
    public DirectExchange productExchange(){
        return new DirectExchange(productExchange,true,false);
    }
    @Bean
    public Binding productBinding(){
        return BindingBuilder.bind(productQueue())
                .to(productExchange())
                .with(productRoutingKey);
    }

    @Value("${rabbitmq.user.available.queue.name}")
    private String userQueue;

    @Value("${rabbitmq.user.available.queue.exchange}")
    private String userExchange;

    @Value("${rabbitmq.user.available.routing.key}")
    private String userRoutingKey;


    @Bean
    public Queue userQueue(){
        return new Queue(userQueue,true);
    }
    @Bean
    public DirectExchange userExchange(){
        return new DirectExchange(userExchange,true,false);
    }
    @Bean
    public Binding userBinding(){
        return BindingBuilder.bind(userQueue())
                .to(userExchange())
                .with(userRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
