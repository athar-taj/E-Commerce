package com.example.category.Controller;

import com.example.category.Model.Request.CategoryRequest;
import com.example.category.Model.Response.CommonResponse;
import com.example.category.Service.CategoryService;
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
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    Validator validator;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> addCategory(@RequestPart("category") @Valid String CategoryRequestData,
                                                      @RequestParam("image") MultipartFile image) throws IOException, ConstraintViolationException {
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest categoryRequest = objectMapper.readValue(CategoryRequestData, CategoryRequest.class);

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(categoryRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return categoryService.addCategory(categoryRequest,image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> updateCategory(@PathVariable Long id, @RequestPart("category") @Valid String CategoryRequestData,
                                                         @RequestParam("image") MultipartFile image) throws IOException, ConstraintViolationException {

        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest categoryRequest = objectMapper.readValue(CategoryRequestData, CategoryRequest.class);

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(categoryRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return categoryService.updateCategory(id, categoryRequest, image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/parents")
    public ResponseEntity<CommonResponse> getAllParentCategories() {
        return categoryService.getAllParentCategories();
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<CommonResponse> getSubCategories(@PathVariable Long parentId) {
        return categoryService.getSubCategoriesByParentId(parentId);
    }

    @GetMapping("/isAvailable/{category}")
    public Boolean isCategoryAvailable(@PathVariable String category){
        return categoryService.isCategoryAvailable(category);
    }
}