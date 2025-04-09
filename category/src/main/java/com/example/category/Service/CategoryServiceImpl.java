package com.example.category.Service;
import com.example.category.Model.Category;
import com.example.category.Model.Request.CategoryRequest;
import com.example.category.Model.Response.CommonResponse;
import com.example.category.Repository.CategoryRepository;
import com.example.category.Service.OtherImpl.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<CommonResponse> addCategory(CategoryRequest request, MultipartFile file) throws IOException {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResponse(409, "Category name already exists! Please use another name.", false));
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getParentId() != null) {
            Optional<Category> parent = categoryRepository.findById(request.getParentId());
            if (parent.isPresent()) {
                category.setParentCategory(parent.get());
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse(404, "Parent Category Not Found !!", false));

            }
        }
        category.setImageUrl(FileStorage.saveFile(file,"category"));
        category.setActive(Boolean.TRUE);
        category.setCreatedAt(LocalDateTime.now());

        categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(201, "Category added successfully!", true));
    }

    @Override
    public ResponseEntity<CommonResponse> updateCategory(Long id, CategoryRequest request,MultipartFile file) throws IOException {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "Category not found!", null));
        }
        Category newCategory = existingCategory.get();

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CommonResponse(409, "Category name already exists! Please use another name.", false));
        }

        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());
        newCategory.setImageUrl(FileStorage.saveFile(file,"category"));

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResponse(400, "Category cannot be its own parent.", false));
            }

            Optional<Category> parentOpt = categoryRepository.findById(request.getParentId());
            if (parentOpt.isPresent()) {
                newCategory.setParentCategory(parentOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CommonResponse(404, "Parent Category Not Found !!", false));
            }
        } else {
            newCategory.setParentCategory(null);
        }

        categoryRepository.save(newCategory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Category updated successfully!", true));
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

    public ResponseEntity<CommonResponse> getSubCategoriesByParentId(Long parentId) {
        List<Category> subCategories = categoryRepository.findByParentCategory_Id(parentId);
        if (subCategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(404, "No subcategories found for given parent ID!", null));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(200, "Subcategories fetched successfully!", subCategories));
    }

    public ResponseEntity<CommonResponse> getAllParentCategories() {
        List<Category> parentCategories = categoryRepository.findByParentCategoryIsNull();
        return ResponseEntity.ok(new CommonResponse(200, "Parent categories fetched successfully", parentCategories));
    }

    public Boolean isCategoryAvailable(String name){
        return categoryRepository.existsByNameIgnoreCase(name);
    }
}
