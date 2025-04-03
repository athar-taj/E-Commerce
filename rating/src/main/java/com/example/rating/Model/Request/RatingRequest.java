package com.example.rating.Model.Request;

import jakarta.validation.constraints.*;

public class RatingRequest {

    @Positive
    @NotNull(message = "Product ID is required")
    private long productId;
    @Positive
    @NotNull(message = "User ID is required")
    private long userId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Title for Product Review is Required")
    private String title;
    private String comment;



    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public @Positive long getUserId() {
        return userId;
    }

    public void setUserId(@Positive long userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
