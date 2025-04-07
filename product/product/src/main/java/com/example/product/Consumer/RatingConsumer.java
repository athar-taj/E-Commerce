package com.example.product.Consumer;

import com.example.product.Model.Product;
import com.example.product.Model.Rating;
import com.example.product.Repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingConsumer {
    @Autowired
    ProductRepository productRepository;

    public Rating ratingConsumerData;

    public Rating getRatingConsumerData() {
        return ratingConsumerData;
    }

    public void setRatingConsumerData(Rating ratingConsumerData) {
        this.ratingConsumerData = ratingConsumerData;
    }

    @RabbitListener(queues = {"${rabbitmq.json.rating.queue}"})
    public void ratingConsumer(Rating rating) {
        System.out.println("Consume Rating -> " + rating.toString());

        Optional<Product> product = productRepository.findById(rating.getProductId());
        if (product.isPresent()) {
            Product prod = product.get();

            prod.setTotalRatings(prod.getTotalRatings() + 1);
            double newRating = ((prod.getProductRating() * (prod.getTotalRatings() - 1)) + rating.getRating()) / prod.getTotalRatings();

            double formattedRating = (double) Math.round(newRating * 10) / 10;
            prod.setProductRating(formattedRating);

            productRepository.save(prod);
            System.out.println("Updated Product Rating -> " + formattedRating);
        } else {
            System.out.println("Product not found for ID: " + rating.getProductId());
        }
    }
}
