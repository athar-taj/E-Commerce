package com.example.rating.Service;

import com.example.rating.Model.Rating;
import com.example.rating.Model.Request.RatingRequest;
import com.example.rating.Model.Response.CommonResponse;
import com.example.rating.Producer.RatingProducer;
import com.example.rating.Repository.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RatingServiceImple implements RatingService {

    private static final Logger log = LoggerFactory.getLogger(RatingServiceImple.class);
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private RatingProducer ratingProducer;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<CommonResponse> addRating(RatingRequest request) {
        List<Rating> UserRating = ratingRepository.findByUserId(request.getUserId());
        for(Rating rating : UserRating){
            if(rating.getProductId() == request.getProductId() && rating.getUserId() == request.getUserId()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonResponse(HttpStatus.UNAUTHORIZED.value(), "You Already Rated This Product!", false));
            }
        }

        Rating rating = new Rating();
        rating.setProductId(request.getProductId());
        rating.setUserId(request.getUserId());
        rating.setRating(request.getRating());
        rating.setTitle(request.getTitle());
        rating.setComment(request.getComment());
        rating.setTime(LocalDateTime.now());

        Rating savedRating = ratingRepository.save(rating);
        ratingProducer.sendRatingBody(savedRating);


        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Rating added successfully!", savedRating));
    }

    @Override
    public ResponseEntity<CommonResponse> getRatingsByUser(long userId) {
        List<Rating> ratings = ratingRepository.findByUserId(userId);
        if (ratings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "No ratings found for the User ID: " + userId, null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Ratings fetched successfully!", ratings));
    }

    @Override
    public ResponseEntity<CommonResponse> getRatingsByProduct(long productId) {
        List<Rating> ratings = ratingRepository.findByProductId(productId);
        if (ratings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "No ratings found for the product ID: " + productId, null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Ratings fetched successfully!", ratings));
    }

    @Override
    public ResponseEntity<CommonResponse> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        if (ratings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "No ratings found!", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "All ratings fetched successfully!", ratings));
    }

    @Override
    public ResponseEntity<CommonResponse> updateRating(long id, RatingRequest request) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);

        if (optionalRating.isPresent()) {
            Rating rating = optionalRating.get();
            rating.setRating(request.getRating());
            rating.setTitle(request.getTitle());
            rating.setComment(request.getComment());
            rating.setTime(LocalDateTime.now());
            Rating updatedRating = ratingRepository.save(rating);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Rating updated successfully!", updatedRating));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Rating not found for ID: " + id, null));
        }
    }

    @Override
    public ResponseEntity<CommonResponse> deleteRating(long id) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);

        if (optionalRating.isPresent()) {
            ratingRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Rating deleted successfully!", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Rating not found for ID: " + id, null));
        }
    }

}