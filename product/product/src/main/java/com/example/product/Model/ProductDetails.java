package com.example.product.Model;

import java.util.List;

public class ProductDetails {

    private String id;
    private Long productId;
    private List<ProductSubDetailRequest> subDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<ProductSubDetailRequest> getSubDetails() {
        return subDetails;
    }

    public void setSubDetails(List<ProductSubDetailRequest> subDetails) {
        this.subDetails = subDetails;
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                ", subDetails=" + subDetails +
                '}';
    }
}


