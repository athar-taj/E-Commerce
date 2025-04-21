package com.example.rating.Controller;

import com.example.rating.Model.Rating;
import com.example.rating.Model.Request.RatingRequest;
import com.example.rating.Model.Response.CommonResponse;
import com.example.rating.Service.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private static final Logger log = LoggerFactory.getLogger(RatingController.class);
    @Autowired
    private RatingService ratingService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addRating(@Valid @RequestBody RatingRequest request) {
        Rating rating = new Rating();
        rating.setProductId(request.getProductId());
        rating.setUserId(request.getUserId());
        rating.setRating(request.getRating());
        rating.setTitle(request.getTitle());
        rating.setComment(request.getComment());
        rating.setTime(LocalDateTime.now());

        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("type", "rating");
            logData.put("productId", rating.getProductId());
            logData.put("userId", rating.getUserId());
            logData.put("rating", rating.getRating());
            logData.put("title", rating.getTitle());
            logData.put("comment", rating.getComment());
            logData.put("timestamp", rating.getTime().toString());

            String jsonLog = objectMapper.writeValueAsString(logData);
            log.info(jsonLog);
        } catch (Exception e) {
            log.error("Error Occuring", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Rating added successfully! ", rating));
    }


//    @PostMapping("/add")
//    public ResponseEntity<CommonResponse> addRating(@Valid @RequestBody RatingRequest request) {
//        Boolean isProductAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
//                "rating_exchange", "product_routing_key", request.getProductId()
//        );
//
//        Boolean isUserAvailable = (Boolean) rabbitTemplate.convertSendAndReceive(
//                "rating_exchange", "user_routing_key", request.getUserId()
//        );
//
//        if(Boolean.TRUE.equals(isUserAvailable)){
//            if (Boolean.TRUE.equals(isProductAvailable)){
//                return ratingService.addRating(request);
//
//            }else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new CommonResponse(HttpStatus.NOT_FOUND.value(), "Product Is Not Available !", false));
//            }
//        }else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new CommonResponse(HttpStatus.NOT_FOUND.value(), "User Is Not Available !", false));
//        }
//    }

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

