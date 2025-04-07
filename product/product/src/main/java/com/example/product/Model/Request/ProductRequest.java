package com.example.product.Model.Request;


import jakarta.validation.constraints.*;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @Positive(message = "Price must be greater than zero")
    private int price;

    @PositiveOrZero(message = "Stock cannot be negative")
    private int stock;

    @NotBlank(message = "Category is required")
    private String category;

    private String brand;

    private String specifications;

    private String about;

    @Min(value = 0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot exceed 100")
    private int discount;


    public @NotBlank(message = "Product name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Product name is required") String name) {
        this.name = name;
    }

    @Positive(message = "Price must be greater than zero")
    public int getPrice() {
        return price;
    }

    public void setPrice(@Positive(message = "Price must be greater than zero") int price) {
        this.price = price;
    }

    @PositiveOrZero(message = "Stock cannot be negative")
    public int getStock() {
        return stock;
    }

    public void setStock(@PositiveOrZero(message = "Stock cannot be negative") int stock) {
        this.stock = stock;
    }

    public @NotBlank(message = "Category is required") String getCategory() {
        return category;
    }

    public void setCategory(@NotBlank(message = "Category is required") String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Min(value = 0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot exceed 100")
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(@Min(value = 0, message = "Discount cannot be negative") @Max(value = 100, message = "Discount cannot exceed 100") int discount) {
        this.discount = discount;
    }
}
