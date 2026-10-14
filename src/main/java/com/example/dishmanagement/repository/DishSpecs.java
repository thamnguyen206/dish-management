package com.example.dishmanagement.repository;

import com.example.dishmanagement.dto.DishFilter;
import com.example.dishmanagement.entity.Dish_BIT240269;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/** Mọi điều kiện lọc được dịch thành câu SQL (WHERE / ORDER BY), không lọc bằng vòng lặp trong Java. */
public final class DishSpecs {

    private DishSpecs() {}

    public static Specification<Dish_BIT240269> build(DishFilter f) {
        return (root, query, cb) -> {
            // Nạp luôn category để tránh N+1 (chỉ khi không phải câu count)
            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("category", JoinType.LEFT);
            }

            List<Predicate> ps = new ArrayList<>();

            if (f.getKeyword() != null && !f.getKeyword().isBlank()) {
                ps.add(cb.like(cb.lower(root.get("name")), "%" + f.getKeyword().trim().toLowerCase() + "%"));
            }
            if (f.getCategoryId() != null) {
                ps.add(cb.equal(root.get("category").get("id"), f.getCategoryId()));
            }
            if ("available".equals(f.getStatus())) {
                ps.add(cb.isTrue(root.get("isAvailable")));
            } else if ("unavailable".equals(f.getStatus())) {
                ps.add(cb.isFalse(root.get("isAvailable")));
            }
            if (f.getMinPrice() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("price"), f.getMinPrice()));
            }
            if (f.getMaxPrice() != null) {
                ps.add(cb.lessThanOrEqualTo(root.get("price"), f.getMaxPrice()));
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }

    public static Sort sort(String sort) {
        if (sort == null) return Sort.by("id").ascending();
        return switch (sort) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "time_asc" -> Sort.by("preparationTime").ascending();
            default -> Sort.by("id").ascending();
        };
    }
}
