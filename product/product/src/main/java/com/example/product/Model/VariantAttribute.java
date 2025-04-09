package com.example.product.Model;

import com.example.product.Model.Enums.Variants;
import jakarta.persistence.*;

@Entity
@Table(name = "variant_attributes")
public class VariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Variants variantType;

    @Column(nullable = false)
    private String variantValue;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Variants getVariantType() {
        return variantType;
    }

    public void setVariantType(Variants variantType) {
        this.variantType = variantType;
    }

    public String getVariantValue() {
        return variantValue;
    }

    public void setVariantValue(String variantValue) {
        this.variantValue = variantValue;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }
}