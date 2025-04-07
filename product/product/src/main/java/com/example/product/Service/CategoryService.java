package com.example.product.Service;

import com.example.product.Model.Request.CategoryRequest;
import com.example.product.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<CommonResponse> addCategory(CategoryRequest request);
    ResponseEntity<CommonResponse> updateCategory(Long id, CategoryRequest request);
    ResponseEntity<CommonResponse> getCategoryById(Long id);
    ResponseEntity<CommonResponse> getAllCategories();
    ResponseEntity<CommonResponse> deleteCategory(Long id);
}

