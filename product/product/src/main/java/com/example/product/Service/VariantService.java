package com.example.product.Service;

import com.example.product.Model.Request.VariantRequest;
import com.example.product.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VariantService {
    ResponseEntity<CommonResponse> createVariant(VariantRequest request, MultipartFile image) throws IOException;
    ResponseEntity<CommonResponse> updateVariant(Long id, VariantRequest request,MultipartFile image) throws IOException;
    ResponseEntity<CommonResponse> deleteVariant(Long id);
    ResponseEntity<CommonResponse> getProductWithVariants(Long id);
    ResponseEntity<CommonResponse> getProductUsingVariants(Long productId,String color, String size, String storage, String quantity);
}
