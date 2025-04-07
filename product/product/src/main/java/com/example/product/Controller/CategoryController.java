package com.example.product.Controller;

import com.example.product.Model.Request.CategoryRequest;
import com.example.product.Model.Response.CommonResponse;
import com.example.product.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CommonResponse> addCategory(@RequestBody CategoryRequest request) {
        return categoryService.addCategory(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return categoryService.updateCategory(id, request);
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
}