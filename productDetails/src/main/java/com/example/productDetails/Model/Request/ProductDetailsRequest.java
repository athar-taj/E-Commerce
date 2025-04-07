package com.example.productDetails.Model.Request;

import java.util.List;

public class ProductDetailsRequest {
        private Long productId;
        private List<ProductSubDetailRequest> subDetails;

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
}
