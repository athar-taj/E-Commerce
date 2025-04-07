package com.example.order.Service;


import com.example.order.Consumer.OrderConsumer;
import com.example.order.Model.Order;
import com.example.order.Model.Product;
import com.example.order.Model.Request.OrderRequest;
import com.example.order.Model.Request.StockRequest;
import com.example.order.Model.Response.CommonResponse;
import com.example.order.Producer.OrderProducer;
import com.example.order.Repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImple implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    private OrderConsumer orderConsumer;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public ResponseEntity<CommonResponse> placeOrder(OrderRequest orderRequest) {
        Product product = orderConsumer.getProduct();
        Order order = new Order();

        if(product != null) {
            double discountedPrice = product.getPrice() - ((double) (product.getPrice() * product.getDiscount()) / 100);
            order.setTotalPrice(discountedPrice);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not Found in Order !!", null));
        }
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());
        order.setName(orderRequest.getName());
        order.setPhone(orderRequest.getPhone());
        order.setAddress(orderRequest.getAddress());
        order.setCity(orderRequest.getCity());
        order.setState(orderRequest.getState());
        order.setPostalCode(orderRequest.getPostalCode());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        Order savedOrder = orderRepository.save(order);
        orderProducer.sendOrderHeaderBody(order);
        orderConsumer.setProduct(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Order placed successfully!", savedOrder));
    }

    public ResponseEntity<CommonResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Order List!", orders));
    }

    public ResponseEntity<CommonResponse> getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(value -> ResponseEntity.status(HttpStatus.OK)
                        .body(new CommonResponse(200, "Order Details!", value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse(404, "Order not found!", null)));
    }

    public ResponseEntity<CommonResponse> cancelOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus("CANCELLED");
            orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Order cancelled successfully.", true));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse(404, "Order not found!", null));
    }

    public ResponseEntity<CommonResponse> updateStatus(Long orderId, String status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setStatus(status);
            orderRepository.save(order.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Order status updated!", order.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse(404, "Order not found!", null));
    }

    public boolean checkAndUpdateStock(StockRequest  message) {
        Boolean response = (Boolean) rabbitTemplate.convertSendAndReceive("stock_exchange", "stock_routing_key", message);
        return Boolean.TRUE.equals(response);
    }
}
