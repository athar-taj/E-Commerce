package com.example.cart.Service;

import com.example.cart.Model.Request.CartRequest;
import com.example.cart.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<CommonResponse> addToCart(CartRequest request);
    ResponseEntity<CommonResponse> getUserCart(Long userId);
    ResponseEntity<CommonResponse> updateQuantity(CartRequest request);
    ResponseEntity<CommonResponse> removeItem(Long userId, Long productId);
    ResponseEntity<CommonResponse> clearCart(Long userId);
}
