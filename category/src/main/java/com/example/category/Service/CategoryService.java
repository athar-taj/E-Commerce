package com.example.category.Service;

import com.example.category.Model.Request.CategoryRequest;
import com.example.category.Model.Response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService {
    ResponseEntity<CommonResponse> addCategory(CategoryRequest request, MultipartFile file) throws IOException;
    ResponseEntity<CommonResponse> updateCategory(Long id, CategoryRequest request,MultipartFile file)throws IOException;
    ResponseEntity<CommonResponse> getCategoryById(Long id);
    ResponseEntity<CommonResponse> getAllCategories();
    ResponseEntity<CommonResponse> deleteCategory(Long id);
    ResponseEntity<CommonResponse> getSubCategoriesByParentId(Long parentId);
    ResponseEntity<CommonResponse> getAllParentCategories();
    Boolean isCategoryAvailable(String name);
}
