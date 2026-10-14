package com.example.dishmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Model Dish_{MSSV}: Id, Name, Price, PreparationTime, IsAvailable, Description, DishCategoryId */
@Entity
@Table(name = "BIT240269_Dishes",
        uniqueConstraints = @UniqueConstraint(name = "UQ_BIT240269_Dish_Name_Category",
                columnNames = {"BIT240269_Name", "BIT240269_DishCategoryId"}))
@Getter
@Setter
public class Dish_BIT240269 {

    /** Ngưỡng phân loại: <= 15 phút là Quick, > 15 phút là Regular */
    public static final int QUICK_MAX_MINUTES = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240269_Id")
    private Long id;

    @Column(name = "BIT240269_Name", nullable = false, length = 200)
    private String name;

    @Column(name = "BIT240269_Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    // Số phút
    @Column(name = "BIT240269_PreparationTime", nullable = false)
    private Integer preparationTime;

    @Column(name = "BIT240269_IsAvailable", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "BIT240269_Description", length = 2000)
    private String description;

    // Cột khóa ngoại dạng đọc (để có thuộc tính DishCategoryId theo đề); việc ghi thực hiện qua quan hệ "category"
    @Column(name = "BIT240269_DishCategoryId", insertable = false, updatable = false)
    private Long dishCategoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BIT240269_DishCategoryId", nullable = false)
    private DishCategory_BIT240269 category;

    // Một Dish có nhiều DishImage
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @BatchSize(size = 20)
    private List<DishImage_BIT240269> images = new ArrayList<>();

    // ---- Các giá trị chỉ tính khi hiển thị (KHÔNG có cột trong DB) ----

    /** Khóa thông báo i18n: time.quick (<= 15 phút) hoặc time.regular (> 15 phút) */
    public String getTimeTypeKey() {
        return preparationTime != null && preparationTime <= QUICK_MAX_MINUTES ? "time.quick" : "time.regular";
    }

    public boolean isQuick() {
        return preparationTime != null && preparationTime <= QUICK_MAX_MINUTES;
    }

    public String getStatusKey() {
        return Boolean.TRUE.equals(isAvailable) ? "status.available" : "status.unavailable";
    }

    /** Ảnh đại diện; nếu chưa chọn thì null */
    public DishImage_BIT240269 getThumbnailImage() {
        return images.stream().filter(i -> Boolean.TRUE.equals(i.getIsThumbnail())).findFirst().orElse(null);
    }
}
