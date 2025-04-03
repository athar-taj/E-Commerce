package com.example.product.Config;

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

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.json.queue.name}")
    private String jsonQueue;

    @Value("${rabbitmq.json.routing.key}")
    private String jsonRoutingKey;


    // spring bean for rabbitMQ queue
    @Bean
    public Queue queue(){
        return new Queue(queue,true);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange,true,false);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue())
                             .to(exchange())
                             .with(routingKey);
    }


    @Bean
    public Queue jsonQueue(){
        return new Queue(jsonQueue,true);
    }

    @Bean
    public Binding jsonBinding(){
        return BindingBuilder.bind(jsonQueue())
                .to(exchange())
                .with(jsonRoutingKey);
    }

    //ConnectionFactory
    //RabbitTamplate
    //RabbitAdmin

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
