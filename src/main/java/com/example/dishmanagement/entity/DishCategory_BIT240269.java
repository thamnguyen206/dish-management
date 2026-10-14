package com.example.dishmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** Model DishCategory_{MSSV}: Id, Name, Description */
@Entity
@Table(name = "BIT240269_DishCategories")
@Getter
@Setter
public class DishCategory_BIT240269 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240269_Id")
    private Long id;

    @Column(name = "BIT240269_Name", nullable = false, length = 200)
    private String name;

    @Column(name = "BIT240269_Description", length = 1000)
    private String description;

    // Một DishCategory có nhiều Dish
    @OneToMany(mappedBy = "category")
    private List<Dish_BIT240269> dishes = new ArrayList<>();
}
