package com.example.category.Repository;


import com.example.category.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameIgnoreCase(String name);
    List<Category> findAllByIsActiveTrue();
    List<Category> findByParentCategory_Id(Long parentId);
    List<Category> findByParentCategoryIsNull();
}
