package com.example.product.Controller;

import com.example.product.Model.Request.WishlistRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addToWishlist(@RequestBody @Valid WishlistRequest request) {
        return wishlistService.addToWishlist(request);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CommonResponse> removeFromWishlist(@RequestParam Long userId,
                                                             @RequestParam Long productId) {
        return wishlistService.removeFromWishlist(userId, productId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getWishlist(@PathVariable Long userId) {
        return wishlistService.getWishlist(userId);
    }
}






