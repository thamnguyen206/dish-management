package com.example.dishmanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DishFilter {
    private String keyword;
    private Long categoryId;
    /** "" = tất cả, "available" = đang bán, "unavailable" = ngừng bán */
    private String status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    /** price_asc | price_desc | time_asc */
    private String sort;
}
