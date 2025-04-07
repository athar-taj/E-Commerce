package com.example.productDetails.Config;

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

    @Value("${rabbitmq.product.details.queue.name}")
    private String productDetailsQueue;

    @Value("${rabbitmq.product.details.queue.exchange}")
    private String productDetailsExchange;

    @Value("${rabbitmq.product.details.routing.key}")
    private String productDetailsRoutingKey;


    @Bean
    public Queue productDetailsQueue(){
        return new Queue(productDetailsQueue,true);
    }
    @Bean
    public DirectExchange productDetailsExchange(){
        return new DirectExchange(productDetailsExchange,true,false);
    }
    @Bean
    public Binding productDetailsBinding(){
        return BindingBuilder.bind(productDetailsQueue())
                .to(productDetailsExchange())
                .with(productDetailsRoutingKey);
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
