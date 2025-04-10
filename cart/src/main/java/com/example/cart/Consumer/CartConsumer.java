package com.example.cart.Consumer;

import com.example.cart.Model.Cart;
import com.example.cart.Repository.CartRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartConsumer {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${rabbitmq.cart.userId.queue.name}")
    public List<Long> getUserIdToProduct(long userId){
        System.out.println("user id  " + userId);

        List<Cart> cartItems = cartRepository.findByUserId(userId);
        List<Long> productIds = new ArrayList<>();

        for (Cart i : cartItems){
            productIds.add(i.getProductId());
        }
        System.out.println("Product Ids "+ productIds);
        return productIds;
    }
}
