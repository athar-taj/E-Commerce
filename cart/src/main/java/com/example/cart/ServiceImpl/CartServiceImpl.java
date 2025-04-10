package com.example.cart.ServiceImpl;

import com.example.cart.Model.Cart;
import com.example.cart.Model.Request.CartRequest;
import com.example.cart.Model.Response.CommonResponse;
import com.example.cart.Repository.CartRepository;
import com.example.cart.Service.CartService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ResponseEntity<CommonResponse> addToCart(CartRequest request) {

        System.out.println("user Id " +request.getUserId());
        Object userResponse = rabbitTemplate.convertSendAndReceive("product_exchange","cart_user_key",request.getUserId());
        if(Boolean.FALSE.equals(userResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "User not found: " + request.getUserId(), null));
        }

        Boolean productResponse = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","cart_product_key",request.getProductId());
        System.out.println(productResponse);
        if(Boolean.FALSE.equals(productResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product not found: " + request.getProductId(), null));
        }


        Optional<Cart> OptionalCart = cartRepository.findByUserIdAndProductId(request.getUserId(),request.getProductId());
        if(OptionalCart.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CommonResponse(409, "Product Already in cart !!", false));

        }

        Cart cart = OptionalCart.get();
        cart.setUserId(request.getUserId());
        cart.setProductId(request.getProductId());
        cart.setQuantity(request.getQuantity());

        cartRepository.save(cart);
        return ResponseEntity.ok(new CommonResponse(200, "Product added to cart", cart));
    }

    public ResponseEntity<CommonResponse> getUserCart(Long userId) {
        Boolean userResponse = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","cart_user_key",userId);
        System.out.println(userResponse);
        if(Boolean.FALSE.equals(userResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "User not found: " + userId, null));
        }

        List<Cart> cartItems = cartRepository.findByUserId(userId);
        return ResponseEntity.ok(new CommonResponse(200, "Cart items fetched", cartItems));
    }

    public  ResponseEntity<CommonResponse> updateQuantity(CartRequest request) {

        Boolean userResponse = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","cart_user_key",request.getUserId());
        System.out.println(userResponse);
        if(Boolean.FALSE.equals(userResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "User not found: " + request.getUserId(), null));
        }

        Boolean productResponse = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","cart_product_key",request.getProductId());
        System.out.println(productResponse);
        if(Boolean.FALSE.equals(productResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product not found: " + request.getUserId(), null));
        }

        Optional<Cart> cart = cartRepository.findByUserIdAndProductId(request.getUserId(),request.getProductId());
        if (cart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product not in cart", null));
        }

        Cart c = cart.get();
        c.setQuantity(request.getQuantity());
        cartRepository.save(c);

        return ResponseEntity.ok(new CommonResponse(200, "Quantity updated", c));
    }

    @Transactional
    public ResponseEntity<CommonResponse> removeItem(Long userId, Long productId) {

        Boolean userResponse = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","cart_user_key",userId);
        System.out.println(userResponse);
        if(Boolean.FALSE.equals(userResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "User not found: " + userId, null));
        }

        Boolean productResponse = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","cart_product_key",productId);
        System.out.println(productResponse);
        if(Boolean.FALSE.equals(productResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Product not found: " + productId, null));
        }

        cartRepository.deleteByUserIdAndProductId(userId,productId);

        return ResponseEntity.ok(new CommonResponse(200, "Item removed from cart", null));
    }

    @Transactional
    public ResponseEntity<CommonResponse> clearCart(Long userId) {

        Boolean productResponse = (Boolean) rabbitTemplate.convertSendAndReceive("product_exchange","cart_user_key",userId);
        System.out.println(productResponse);
        if(Boolean.FALSE.equals(productResponse)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "User not found: " + userId, null));
        }

        cartRepository.deleteByUserId(userId);

        return ResponseEntity.ok(new CommonResponse(200, "Cart cleared", null));
    }
}