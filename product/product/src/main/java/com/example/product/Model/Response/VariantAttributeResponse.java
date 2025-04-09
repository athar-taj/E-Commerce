package com.example.product.Model.Response;

import com.example.product.Model.Enums.Variants;

public class VariantAttributeResponse {
    private Variants variantType;
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

