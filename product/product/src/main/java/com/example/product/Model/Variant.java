package com.example.product.Model;

import com.example.product.Model.Enums.Variants;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table
@Entity
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String variantName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    private boolean isActive = true;

    @Column(nullable = false)
    private String imageUrl;

    private LocalDateTime createdAt;
}
