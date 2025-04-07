package com.example.product.ServiceImple;

import com.example.product.Model.Category;
import com.example.product.Model.Request.CategoryRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Repository.CategoryRepository;
import com.example.product.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<CommonResponse> addCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResponse(409, "Category name is already taken!", false));
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(Boolean.TRUE);
        category.setCreatedAt(LocalDateTime.now());

        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Category added successfully!", true));
    }

    @Override
    public ResponseEntity<CommonResponse> updateCategory(Long id, CategoryRequest request) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setDescription(request.getDescription());
            categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Category updated successfully!", category));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Category not found!", null));
        }
    }

    @Override
    public ResponseEntity<CommonResponse> getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Category found!", category.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Category not found!", null));
        }
    }

    @Override
    public ResponseEntity<CommonResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByIsActiveTrue();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Categories retrieved successfully!", categories));
    }

    @Override
    public ResponseEntity<CommonResponse> deleteCategory(Long id) {
        boolean existsCategory = categoryRepository.existsById(id);
        if (existsCategory) {
            Optional<Category> category = categoryRepository.findById(id);
            category.get().setActive(Boolean.FALSE);
            categoryRepository.save(category.get());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse(200, "Category Removed successfully!", true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Category not found!", false));
        }
    }
}
