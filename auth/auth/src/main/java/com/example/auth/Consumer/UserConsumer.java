package com.example.auth.Consumer;

import com.example.auth.Repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {
    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = "${rabbitmq.user.queue.name}")
    public Boolean isUserAvailable(long userId) {
        System.out.println("Checking user availability for: " + userId);
        return userRepository.existsById(userId);
    }
}
