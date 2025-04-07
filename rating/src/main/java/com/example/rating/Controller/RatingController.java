package com.example.rating.Controller;

import com.example.rating.Model.Request.RatingRequest;
import com.example.rating.Model.Response.CommonResponse;
import com.example.rating.Service.RatingService;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addRating(@Valid @RequestBody RatingRequest request) {
        Boolean isProductAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
                "rating_exchange", "product_routing_key", request.getProductId()
        );

        Boolean isUserAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
                "rating_exchange", "user_routing_key", request.getUserId()
        );

        if(Boolean.TRUE.equals(isUserAvailable)){
            if (Boolean.TRUE.equals(isProductAvailable)){
                return ratingService.addRating(request);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse(HttpStatus.NOT_FOUND.value(), "Product Is Not Available !", false));
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(HttpStatus.NOT_FOUND.value(), "User Is Not Available !", false));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<CommonResponse> getRatingsByProduct(@PathVariable long productId) {
        return ratingService.getRatingsByProduct(productId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getRatingsByUser(@PathVariable long userId) {
        return ratingService.getRatingsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CommonResponse> updateRating(@PathVariable long id, @Valid @RequestBody RatingRequest request) {
        return ratingService.updateRating(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse> deleteRating(@PathVariable long id) {
        return ratingService.deleteRating(id);
    }
}

