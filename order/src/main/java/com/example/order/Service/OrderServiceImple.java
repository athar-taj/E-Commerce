package com.example.order.Service;


import com.example.order.Consumer.OrderConsumer;
import com.example.order.Model.Order;
import com.example.order.Model.Product;
import com.example.order.Model.Request.CartOrderRequest;
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
import java.util.ArrayList;
import java.util.HashMap;
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
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not Found in Order !! Publish Your order.", null));
        }

        Boolean isUserAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
                "order_exchange", "user_logged_key", orderRequest.getUserId());

        if (Boolean.FALSE.equals(isUserAvailable)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(HttpStatus.NOT_FOUND.value(),
                            "User Not Found !!", false));
        }

        StockRequest stockRequest = new StockRequest();
        stockRequest.setProductId(orderRequest.getProductId());
        stockRequest.setQuantity(orderRequest.getQuantity());

        Boolean isStockAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
                "stock_exchange", "stock_routing_key", stockRequest);

        if (Boolean.FALSE.equals(isStockAvailable)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(HttpStatus.BAD_REQUEST.value(),
                            "Product out of stock!", false));
        }

        double discountedPrice = product.getPrice() - ((double) (product.getPrice() * product.getDiscount()) / 100);

        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
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
        order.setTotalPrice(discountedPrice);

        Order savedOrder = orderRepository.save(order);
        rabbitTemplate.convertAndSend("stock_exchange", "stock_reduce_key", stockRequest);

        orderProducer.sendOrderHeaderBody(order);

        orderConsumer.setProduct(null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Order placed successfully!", savedOrder));
    }

    public ResponseEntity<CommonResponse> placeCartOrder(CartOrderRequest orderRequest) {
        Boolean isUserAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
                "order_exchange", "user_logged_key", orderRequest.getUserId());

        if (Boolean.FALSE.equals(isUserAvailable)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(HttpStatus.NOT_FOUND.value(), "User Not Found !!", false));
        }

        List<Long> cartProductIds = (List<Long>) rabbitTemplate.convertSendAndReceive(
                "order_exchange", "cart_order_products_key", orderRequest.getUserId());

        System.out.println(cartProductIds);
        if (cartProductIds == null || cartProductIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(HttpStatus.BAD_REQUEST.value(), "Cart is empty!", null));
        }

        List<Order> savedOrders = new ArrayList<>();
        List<Long> productUserId = new ArrayList<>();
        productUserId.add(orderRequest.getUserId());

        for (Long productId : cartProductIds) {
            productUserId.add(productId);
            int Quantity = (int) rabbitTemplate.convertSendAndReceive("product_exchange","get_quantity_by_product_id_key",productUserId);

            System.out.println(Quantity);
            rabbitTemplate.convertAndSend("order_exchange","cart_products_key",productId);

            StockRequest stockRequest = new StockRequest();
            stockRequest.setProductId(productId);
            stockRequest.setQuantity(Quantity);

            Boolean isStockAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
                    "stock_exchange", "stock_routing_key", stockRequest);

            if (Boolean.FALSE.equals(isStockAvailable)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResponse(HttpStatus.BAD_REQUEST.value(), "Stock Not Sufficient For Product : " + productId, null));
            }

            double discountedPrice = OrderConsumer.discountedPrice;

            Order order = new Order();
            order.setUserId(orderRequest.getUserId());
            order.setProductId(productId);
            order.setQuantity(Quantity);
            order.setName(orderRequest.getName());
            order.setPhone(orderRequest.getPhone());
            order.setAddress(orderRequest.getAddress());
            order.setCity(orderRequest.getCity());
            order.setState(orderRequest.getState());
            order.setPostalCode(orderRequest.getPostalCode());
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("SUCCESS");
            order.setTotalPrice(discountedPrice);

            Order savedOrder = orderRepository.save(order);
            savedOrders.add(savedOrder);

            rabbitTemplate.convertAndSend("stock_exchange", "stock_reduce_key", stockRequest);

            orderProducer.sendOrderHeaderBody(savedOrder);

            productUserId.remove(1);
        }

        if (savedOrders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse(400, "No orders could be placed. Either out of stock or invalid items.", null));
        }

        rabbitTemplate.convertAndSend("product_exchange","clear_cart_key",orderRequest.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Order placed successfully!", null));
    }

    public ResponseEntity<CommonResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Order List!", orders));
    }

    public ResponseEntity<CommonResponse> getOrderByUserId(Long userId) {
        List<Order> order = orderRepository.findByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Order Fetched successfully.", order));
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
