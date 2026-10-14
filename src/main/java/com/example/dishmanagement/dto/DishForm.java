package com.example.dishmanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Dữ liệu form Create/Edit. Thông báo lỗi KHÔNG viết cứng ở đây: Spring tự tìm trong messages*.properties
 * theo khóa dạng NotBlank.form.name, DecimalMin.form.price ... nên đổi ngôn ngữ là đổi luôn thông báo.
 */
@Getter
@Setter
public class DishForm {

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer preparationTime;

    private Boolean isAvailable = true;

    @Size(max = 2000)
    private String description;

    @NotNull
    private Long dishCategoryId;
}
