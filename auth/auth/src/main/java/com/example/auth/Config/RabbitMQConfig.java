package com.example.auth.Config;

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

    @Value("${rabbitmq.user.queue.name}")
    private String queue;

    @Value("${rabbitmq.user.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.user.routing.key}")
    private String routingKey;

    @Bean
    public DirectExchange authExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue authAvailableQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding authBinding() {
        return BindingBuilder
                .bind(authAvailableQueue())
                .to(authExchange())
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
