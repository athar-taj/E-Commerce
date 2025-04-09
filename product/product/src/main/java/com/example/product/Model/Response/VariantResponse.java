package com.example.product.Model.Response;

import java.util.List;

public class VariantResponse {
    private String variantName;
    private int price;
    private int stock;
    private String imageUrl;
    private List<VariantAttributeResponse> attributes;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<VariantAttributeResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<VariantAttributeResponse> attributes) {
        this.attributes = attributes;
    }
}
