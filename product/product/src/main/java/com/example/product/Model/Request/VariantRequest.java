package com.example.product.Model.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class VariantRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Variant name must not be blank")
    private String variantName;

    @Min(value = 0, message = "Price must be a non-negative value")
    private int price;

    @Min(value = 0, message = "Stock must be a non-negative value")
    private int stock;

    @NotNull(message = "Attributes list is required")
    @Size(min = 1, message = "At least one attribute is required")
    private List<@Valid VariantAttributeRequest> attributes;

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
