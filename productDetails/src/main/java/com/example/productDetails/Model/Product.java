package com.example.productDetails.Model;

import com.example.productDetails.Model.Request.ProductSubDetailRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "products")
public class Product {

        @Id
        private String id;
        @Field(type = FieldType.Text)
        private String product;
        @Field(type = FieldType.Integer)
        private Long productId;
        private List<ProductSubDetailRequest> subDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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
