package com.example.product.Service;

import com.example.product.Model.Request.WishlistRequest;
import com.example.product.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface WishlistService {
    ResponseEntity<CommonResponse> addToWishlist(WishlistRequest request);
    ResponseEntity<CommonResponse> removeFromWishlist(Long userId, Long productId);
    ResponseEntity<CommonResponse> getWishlist(Long userId);
}
