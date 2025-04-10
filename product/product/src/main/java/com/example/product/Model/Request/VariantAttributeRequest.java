package com.example.product.Model.Request;

import com.example.product.Model.Enums.Variants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VariantAttributeRequest {
    @NotNull(message = "Variant type is required")
    private Variants variantType;

    @NotBlank(message = "Variant value must not be blank")
    private String variantValue;

    public Variants getVariantType() {
        return variantType;
    }

    public void setVariantType(Variants variantType) {
        this.variantType = variantType;
    }

    public String getVariantValue() {
        return variantValue;
    }

    public void setVariantValue(String variantValue) {
        this.variantValue = variantValue;
    }
}
