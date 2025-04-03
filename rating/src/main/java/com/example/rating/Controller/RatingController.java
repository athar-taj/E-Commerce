package com.example.rating.Controller;

import com.example.rating.Model.Request.RatingRequest;
import com.example.rating.Model.Response.CommonResponse;
import com.example.rating.Service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addRating(@Valid @RequestBody RatingRequest request) {
        return ratingService.addRating(request);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<CommonResponse> getRatingsByProduct(@PathVariable long productId) {
        return ratingService.getRatingsByProduct(productId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getRatingsByUser(@PathVariable long userId) {
        return ratingService.getRatingsByProduct(userId);
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

