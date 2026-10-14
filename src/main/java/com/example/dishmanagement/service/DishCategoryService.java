package com.example.dishmanagement.service;

import com.example.dishmanagement.dto.DishCategoryForm;
import com.example.dishmanagement.entity.DishCategory_BIT240269;
import com.example.dishmanagement.exception.BusinessException;
import com.example.dishmanagement.exception.ResourceNotFoundException;
import com.example.dishmanagement.repository.DishCategoryRepository;
import com.example.dishmanagement.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishCategoryService {

    private final DishCategoryRepository categoryRepo;
    private final DishRepository dishRepo;

    @Transactional(readOnly = true)
    public List<DishCategory_BIT240269> findAll() {
        return categoryRepo.findAll();
    }

    @Transactional(readOnly = true)
    public DishCategory_BIT240269 getById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.category.notfound", id));
    }

    public DishCategoryForm toForm(DishCategory_BIT240269 c) {
        DishCategoryForm f = new DishCategoryForm();
        f.setName(c.getName());
        f.setDescription(c.getDescription());
        return f;
    }

    /** Tên loại món không được trùng */
    @Transactional(readOnly = true)
    public void validateBusiness(DishCategoryForm form, Long currentId, BindingResult br) {
        if (form.getName() == null || form.getName().isBlank()) return;
        String name = form.getName().trim();
        boolean dup = currentId == null
                ? categoryRepo.existsByNameIgnoreCase(name)
                : categoryRepo.existsByNameIgnoreCaseAndIdNot(name, currentId);
        if (dup) br.rejectValue("name", "category.name.duplicate");
    }

    public DishCategory_BIT240269 save(Long id, DishCategoryForm form) {
        DishCategory_BIT240269 c = id == null ? new DishCategory_BIT240269() : getById(id);
        c.setName(form.getName().trim());
        c.setDescription(form.getDescription());
        return categoryRepo.save(c);
    }

    /** Ràng buộc: không xóa loại món đang có món sử dụng. */
    public void delete(Long id) {
        DishCategory_BIT240269 c = getById(id);
        if (dishRepo.existsByDishCategoryId(id)) {
            throw new BusinessException("error.category.inUse", c.getName());
        }
        categoryRepo.delete(c);
    }
}
