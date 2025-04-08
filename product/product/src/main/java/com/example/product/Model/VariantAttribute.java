package com.example.product.Model;

import jakarta.persistence.*;

@Table
@Entity
public class VariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Variant variantType;

    @Column(nullable = false)
    private String variantValue;

    private Variant variantId;

}
