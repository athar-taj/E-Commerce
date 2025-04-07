package com.example.order.Model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class Product {
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String category;

    private String brand;

    private String specifications;
    private String about;

    private boolean isActive = true;

    private double productRating;

    private int totalRatings;
    private String imageUrl;
    private int discount;

    private LocalDateTime createdAt;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", specifications='" + specifications + '\'' +
                ", about='" + about + '\'' +
                ", isActive=" + isActive +
                ", productRating=" + productRating +
                ", totalRatings=" + totalRatings +
                ", imageUrl='" + imageUrl + '\'' +
                ", discount=" + discount +
                ", createdAt=" + createdAt +
                '}';
    }
}