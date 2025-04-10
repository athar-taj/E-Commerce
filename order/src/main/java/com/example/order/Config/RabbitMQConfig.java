package com.example.order.Config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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
    public DirectExchange exchange(){
        return new DirectExchange(exchange);
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


    // Header Exchange


    @Value("${rabbitmq.headers.exchange}")
    private String headersExchange;

    @Value("${rabbitmq.vip.queue.name}")
    private String vipQueue;

    @Value("${rabbitmq.regular.queue.name}")
    private String regularQueue;

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(headersExchange);
    }

    @Bean
    public Queue vipQueue() {
        return new Queue(vipQueue, true);
    }

    @Bean
    public Queue regularQueue() {
        return new Queue(regularQueue, true);
    }

    @Bean
    public Binding vipBinding() {
        Map<String, Object> headerValues = new HashMap<>();
        headerValues.put("customerType", "VIP");

        return BindingBuilder.bind(vipQueue())
                .to(headersExchange())
                .whereAll(headerValues).match();
    }


    @Bean
    public Binding regularBinding() {
        Map<String, Object> headerValues = new HashMap<>();
        headerValues.put("customerType", "Regular");

        return BindingBuilder.bind(regularQueue())
                .to(headersExchange())
                .whereAll(headerValues).match();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.modules(new JavaTimeModule());
    }


    @Bean
    public Queue reduceStockQueue() {
        return new Queue("reduce_stock_queue");
    }

    @Bean
    public DirectExchange reduceStockExchange() {
        return new DirectExchange("stock_exchange");
    }

    @Bean
    public Binding reduceStockBinding() {
        return BindingBuilder.bind(reduceStockQueue()).to(reduceStockExchange()).with("stock_reduce_key");
    }


    @Value("${rabbitmq.cart.userId.queue.name}")
    private String userQueue;

    @Value("${rabbitmq.cart.userId.queue.exchange}")
    private String userExchange;

    @Value("${rabbitmq.cart.userId.routing.key}")
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
}
