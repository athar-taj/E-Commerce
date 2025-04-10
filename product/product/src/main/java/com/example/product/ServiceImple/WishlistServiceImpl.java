package com.example.product.ServiceImple;

import com.example.product.Model.Product;
import com.example.product.Model.Request.WishlistRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Model.Wishlist;
import com.example.product.Repository.ProductRepository;
import com.example.product.Repository.WishlistRepository;
import com.example.product.Service.WishlistService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepo;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ResponseEntity<CommonResponse> addToWishlist(WishlistRequest request) {
        if (wishlistRepo.existsByUserIdAndProductId(request.getUserId(), request.getProductId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResponse(409, "Already in wishlist!", null));
        }

        Optional<Product> product = productRepository.findById(request.getProductId());
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product Not found!", null));
        }

        Boolean response = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","wishlist_product_routing",request.getUserId());
        System.out.println(response);
        if(Boolean.FALSE.equals(response)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "User not found: " + request.getUserId(), null));
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(request.getUserId());
        wishlist.setProduct(product.get());
        wishlist.setCreatedAt(LocalDateTime.now());

        wishlistRepo.save(wishlist);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Product added to wishlist", true));
    }

    public ResponseEntity<CommonResponse> removeFromWishlist(Long userId, Long productId) {
        wishlistRepo.deleteByUserIdAndProductId(userId, productId);
        return  ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Product removed from wishlist", true));
    }

    public ResponseEntity<CommonResponse> getWishlist(Long userId) {
        List<Wishlist> wishlist = wishlistRepo.findByUserId(userId);
        return  ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Wishlist fetched", wishlist));
    }
}