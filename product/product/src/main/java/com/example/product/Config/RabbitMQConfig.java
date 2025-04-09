package com.example.product.Config;

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


    @Value("${rabbitmq.stock.queue.name}")
    private String stockQueue;

    @Value("${rabbitmq.stock.routing.key}")
    private String stockRoutingKey;

    @Value("${rabbitmq.stock.exchange}")
    private String stockExchange;

    @Bean
    public Queue stockQueue() {
        return new Queue(stockQueue, false);
    }

    @Bean
    public DirectExchange stockExchange() {
        return new DirectExchange(stockExchange);
    }

    @Bean
    public Binding stockBinding(Queue stockQueue, DirectExchange stockExchange) {
        return BindingBuilder.bind(stockQueue).to(stockExchange).with(stockRoutingKey);
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


    @Value("${rabbitmq.product.queue.name}")
    private String productQueue;

    @Value("${rabbitmq.product.queue.exchange}")
    private String productExchange;

    @Value("${rabbitmq.product.routing.key}")
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

    @Value("${rabbitmq.product.suggestion.queue.name}")
    private String productSuggestionQueue;

    @Value("${rabbitmq.product.suggestion.queue.exchange}")
    private String productSuggestionExchange;

    @Value("${rabbitmq.product.suggestion.routing.key}")
    private String productSuggestionRoutingKey;


    @Bean
    public Queue productSuggestionQueue(){
        return new Queue(productSuggestionQueue,true);
    }
    @Bean
    public DirectExchange productSuggestionExchange(){
        return new DirectExchange(productSuggestionExchange,true,false);
    }
    @Bean
    public Binding productSuggestionBinding(){
        return BindingBuilder.bind(productSuggestionQueue())
                .to(productSuggestionExchange())
                .with(productSuggestionRoutingKey);
    }

}
