package com.example.productDetails.Model;

import com.example.productDetails.Model.Request.ProductSubDetailRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "product_details")
public class ProductDetails {

    @Id
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
}


