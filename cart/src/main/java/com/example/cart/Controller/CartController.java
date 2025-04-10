package com.example.cart.Controller;

import com.example.cart.Model.Request.CartRequest;
import com.example.cart.Model.Response.CommonResponse;
import com.example.cart.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addToCart(@RequestBody CartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getUserCart(@PathVariable Long userId) {
        return cartService.getUserCart(userId);
    }

    @PutMapping("/update")
    public ResponseEntity<CommonResponse> updateQuantity(@RequestBody CartRequest request) {
        return cartService.updateQuantity(request);
    }

        @DeleteMapping("/remove")
    public ResponseEntity<CommonResponse> removeItem(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
        return cartService.removeItem(userId, productId);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<CommonResponse> clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }
}

