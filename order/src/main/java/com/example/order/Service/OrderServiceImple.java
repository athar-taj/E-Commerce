package com.example.order.Service;


import com.example.order.Model.Order;
import com.example.order.Model.Response.CommonResponse;
import com.example.order.Producer.OrderProducer;
import com.example.order.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImple implements OrderService  {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProducer orderProducer;

    public ResponseEntity<CommonResponse> placeOrder(Order orderRequest) {

        orderRequest.setOrderDate(LocalDateTime.now());
        orderRequest.setStatus("PENDING");

        Order savedOrder = orderRepository.save(orderRequest);
        orderProducer.sendOrderHeaderBody(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Order Place successfully !!", savedOrder));
    }

    public ResponseEntity<CommonResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Order List !!", orders));
    }

    public ResponseEntity<CommonResponse> getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Order List !!", order));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Order Not Found !!", null));
        }
    }

    public ResponseEntity<CommonResponse> cancelOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus("CANCELLED");
            orderRepository.save(order);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(200, "Order cancelled successfully.", true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Order not found!", null));
        }
    }

    @Override
    public ResponseEntity<CommonResponse> updateStatus(Long orderId, String status) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            order.get().setStatus("SUCCESS");
            orderRepository.save(order.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Order Updated !!", order.get()));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Order Not Found !!", null));
        }
    }

}