package com.example.dishmanagement.repository;

import com.example.dishmanagement.entity.DishCategory_BIT240269;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishCategoryRepository extends JpaRepository<DishCategory_BIT240269, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
