package com.example.dishmanagement.repository;

import com.example.dishmanagement.entity.Dish_BIT240269;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DishRepository extends JpaRepository<Dish_BIT240269, Long>, JpaSpecificationExecutor<Dish_BIT240269> {
    boolean existsByDishCategoryId(Long dishCategoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryId(String name, Long dishCategoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(String name, Long dishCategoryId, Long id);
}
