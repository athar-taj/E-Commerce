package com.example.order.Service;

import com.example.order.Model.Order;
import com.example.order.Model.Request.OrderRequest;
import com.example.order.Model.Request.StockRequest;
import com.example.order.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    ResponseEntity<CommonResponse> placeOrder(OrderRequest order);
    ResponseEntity<CommonResponse> getAllOrders();
    ResponseEntity<CommonResponse> getOrderById(Long orderId);
    ResponseEntity<CommonResponse> cancelOrder(Long orderId);
    ResponseEntity<CommonResponse> updateStatus(Long orderId,String status);
    boolean checkAndUpdateStock(StockRequest stockRequest);
}
