package com.example.dishmanagement.repository;

import com.example.dishmanagement.entity.DishImage_BIT240269;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DishImageRepository extends JpaRepository<DishImage_BIT240269, Long> {

    /** Bỏ ảnh đại diện cũ của món (đảm bảo tối đa 1 ảnh đại diện) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update DishImage_BIT240269 i set i.isThumbnail = false where i.dishId = :dishId and i.isThumbnail = true")
    void clearThumbnail(@Param("dishId") Long dishId);
}
