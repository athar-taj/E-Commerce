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
        return userRepository.existsById(userId);
    }

    @RabbitListener(queues = "${rabbitmq.user.available.queue.name}")
    public Boolean isUserAvailableForOrder(long userId) {
        return userRepository.existsById(userId);
    }

    @RabbitListener(queues = "${rabbitmq.user.available.wishlist.queue.name}")
    public Boolean isUserAvailableForWishlist(long userId) {
        return userRepository.existsById(userId);
    }

    @RabbitListener(queues = "${rabbitmq.cart.user.available.queue.name}")
    public Boolean isUserAvailableForCart(long userId) {
        return userRepository.existsById(userId);
    }
}
