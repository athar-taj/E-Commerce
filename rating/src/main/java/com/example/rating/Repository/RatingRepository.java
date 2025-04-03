package com.example.rating.Repository;

import com.example.rating.Model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    List<Rating> findByProductId(long productId);
    List<Rating> findByUserId(long userId);
}
