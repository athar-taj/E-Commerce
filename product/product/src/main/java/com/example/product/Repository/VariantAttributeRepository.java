package com.example.product.Repository;

import com.example.product.Model.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantAttributeRepository extends JpaRepository<VariantAttribute,Long> {
    Optional<VariantAttribute> findByVariantId(Long id);

    void deleteByVariantId(Long id);

    List<VariantAttribute> findAllByVariantId(Long id);
}
