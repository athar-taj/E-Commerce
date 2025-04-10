package com.example.cart.Model.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 0, message = "Quantity must be a non-negative value")
    private int quantity;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
