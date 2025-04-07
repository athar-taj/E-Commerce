package com.example.product.Model.Request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CategoryRequest {

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Length(max = 1500)
    private String description;


    public @NotBlank(message = "Category name cannot be blank") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Category name cannot be blank") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Description cannot be blank") @Length(max = 1500) String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description cannot be blank") @Length(max = 1500) String description) {
        this.description = description;
    }
}
