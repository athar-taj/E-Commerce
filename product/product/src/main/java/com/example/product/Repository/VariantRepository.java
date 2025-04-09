package com.example.product.Repository;

import com.example.product.Model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant,Long> {
    List<Variant> findByProductId(Long id);
}
