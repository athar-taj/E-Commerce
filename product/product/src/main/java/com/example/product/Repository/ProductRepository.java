package com.example.product.Repository;

import com.example.product.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query(nativeQuery = true,value = "SELECT * FROM ecommerce.products WHERE discount >= :discount")
    List<Product> findByDiscountProduct(@Param("discount") int discount);

    Product findByCategory(String c);
}
