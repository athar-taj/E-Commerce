package com.example.productDetails.Model;

import com.example.productDetails.Model.Request.ProductSubDetailRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "products")
public class Product {

        @Id
        private Integer id;
        private Long productId;
        private List<ProductSubDetailRequest> subDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
