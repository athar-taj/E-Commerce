package com.example.product.Model.Request;

import java.util.List;

public class VariantRequest {
    private Long productId;
    private String variantName;
    private int price;
    private int stock;

    private List<VariantAttributeRequest> attributes;

    public VariantRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<VariantAttributeRequest> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<VariantAttributeRequest> attributes) {
        this.attributes = attributes;
    }
}
