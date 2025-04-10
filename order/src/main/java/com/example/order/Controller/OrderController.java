package com.example.order.Controller;

import com.example.order.Model.Order;
import com.example.order.Model.Request.CartOrderRequest;
import com.example.order.Model.Request.OrderRequest;
import com.example.order.Model.Request.StockRequest;
import com.example.order.Model.Response.CommonResponse;
import com.example.order.Producer.OrderProducer;
import com.example.order.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/place")
    public ResponseEntity<CommonResponse> placeOrder(@RequestBody @Valid OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @PostMapping("/cart/place")
    public ResponseEntity<CommonResponse> placeCartOrder(@RequestBody @Valid CartOrderRequest orderRequest) {
        return orderService.placeCartOrder(orderRequest);
    }

    @PatchMapping("/update-status/{orderId}/{status}")
    public ResponseEntity<CommonResponse> updateOrderStatus(@PathVariable("orderId") Long orderId,@PathVariable("status") String status){
        return orderService.updateStatus(orderId,status);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getOrderByUserId(@PathVariable("userId") Long id){
        return orderService.getOrderByUserId(id);
    }
}
