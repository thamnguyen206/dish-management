package com.example.dishmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** Model DishImage_{MSSV}: Id, ImageUrl, IsThumbnail, DishId */
@Entity
@Table(name = "BIT240269_DishImages")
@Getter
@Setter
public class DishImage_BIT240269 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240269_Id")
    private Long id;

    @Column(name = "BIT240269_ImageUrl", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "BIT240269_IsThumbnail", nullable = false)
    private Boolean isThumbnail = false;

    // Cột khóa ngoại dạng đọc (thuộc tính DishId theo đề); việc ghi thực hiện qua quan hệ "dish"
    @Column(name = "BIT240269_DishId", insertable = false, updatable = false)
    private Long dishId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BIT240269_DishId", nullable = false)
    private Dish_BIT240269 dish;
}
