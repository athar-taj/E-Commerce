package com.example.product.Controller;


import com.example.product.Model.Request.ProductRequest;
import com.example.product.Model.Request.VariantRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Service.VariantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/variants")
public class VariantController {

    @Autowired
    private VariantService variantService;
    @Autowired
    private Validator validator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> createVariant(@RequestPart("variant") @Valid String VariantRequestJson,
                                                        @RequestParam("image") MultipartFile image) throws IOException, ConstraintViolationException {
        ObjectMapper objectMapper = new ObjectMapper();
        VariantRequest variantRequest = objectMapper.readValue(VariantRequestJson, VariantRequest.class);

        Set<ConstraintViolation<VariantRequest>> violations = validator.validate(variantRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return variantService.createVariant(variantRequest,image);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> updateProduct(@PathVariable Long id,
                                                        @RequestPart("variant") @Valid String variantRequestJson,
                                                        @RequestParam(value = "image", required = false) MultipartFile image)
            throws IOException, ConstraintViolationException {

        ObjectMapper objectMapper = new ObjectMapper();
        VariantRequest variantRequest = objectMapper.readValue(variantRequestJson, VariantRequest.class);

        Set<ConstraintViolation<VariantRequest>> violations = validator.validate(variantRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return variantService.updateVariant(id, variantRequest, image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteVariant(@PathVariable Long id) {
        return variantService.deleteVariant(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getProductWithVariants(@PathVariable Long id) {
        return variantService.getProductWithVariants(id);
    }

    @GetMapping("/{productId}/product")
    public ResponseEntity<CommonResponse> getVariantProducts(@PathVariable Long productId,
                                     @RequestParam(value = "Color",required = false) String Color,
                                     @RequestParam(value = "Size",required = false) String Size,
                                     @RequestParam(value = "Storage",required = false) String Storage,
                                     @RequestParam(value = "Quantity",required = false) String Quantity){
        return variantService.getProductUsingVariants(productId,Color,Size,Storage,Quantity);
    }
}

