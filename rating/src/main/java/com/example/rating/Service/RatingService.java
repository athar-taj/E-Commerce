package com.example.rating.Service;

import com.example.rating.Model.Request.RatingRequest;
import com.example.rating.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingService {
    ResponseEntity<CommonResponse> addRating(RatingRequest request);
    ResponseEntity<CommonResponse> getRatingsByProduct(long productId);
    ResponseEntity<CommonResponse> getRatingsByUser(long userId);
    ResponseEntity<CommonResponse> getAllRatings();
    ResponseEntity<CommonResponse> updateRating(long id, RatingRequest request);
    ResponseEntity<CommonResponse> deleteRating(long id);
}
