package com.example.dishmanagement.service;

import com.example.dishmanagement.dto.DishFilter;
import com.example.dishmanagement.dto.DishForm;
import com.example.dishmanagement.entity.DishCategory_BIT240269;
import com.example.dishmanagement.entity.DishImage_BIT240269;
import com.example.dishmanagement.entity.Dish_BIT240269;
import com.example.dishmanagement.exception.BusinessException;
import com.example.dishmanagement.exception.ResourceNotFoundException;
import com.example.dishmanagement.repository.DishCategoryRepository;
import com.example.dishmanagement.repository.DishImageRepository;
import com.example.dishmanagement.repository.DishRepository;
import com.example.dishmanagement.repository.DishSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class DishService {

    private static final Pattern URL_PATTERN = Pattern.compile("^https?://\\S+$", Pattern.CASE_INSENSITIVE);

    private final DishRepository dishRepo;
    private final DishCategoryRepository categoryRepo;
    private final DishImageRepository imageRepo;

    // ---------------- Danh sách / chi tiết ----------------

    @Transactional(readOnly = true)
    public List<Dish_BIT240269> search(DishFilter filter) {
        return dishRepo.findAll(DishSpecs.build(filter), DishSpecs.sort(filter.getSort()));
    }

    @Transactional(readOnly = true)
    public Dish_BIT240269 getById(Long id) {
        return dishRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.dish.notfound", id));
    }

    public DishForm toForm(Dish_BIT240269 d) {
        DishForm f = new DishForm();
        f.setName(d.getName());
        f.setPrice(d.getPrice());
        f.setPreparationTime(d.getPreparationTime());
        f.setIsAvailable(d.getIsAvailable());
        f.setDescription(d.getDescription());
        f.setDishCategoryId(d.getCategory().getId());
        return f;
    }

    // ---------------- Create / Edit ----------------

    /** Kiểm tra quy tắc nghiệp vụ: DishCategoryId tồn tại, tên món không trùng trong cùng loại. */
    @Transactional(readOnly = true)
    public void validateBusiness(DishForm form, Long currentDishId, BindingResult br) {
        if (form.getDishCategoryId() != null && !categoryRepo.existsById(form.getDishCategoryId())) {
            br.rejectValue("dishCategoryId", "dish.category.notfound");
            return;
        }
        if (form.getName() != null && !form.getName().isBlank() && form.getDishCategoryId() != null) {
            String name = form.getName().trim();
            boolean duplicated = currentDishId == null
                    ? dishRepo.existsByNameIgnoreCaseAndDishCategoryId(name, form.getDishCategoryId())
                    : dishRepo.existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(name, form.getDishCategoryId(), currentDishId);
            if (duplicated) {
                br.rejectValue("name", "dish.name.duplicate");
            }
        }
    }

    public Dish_BIT240269 create(DishForm form) {
        Dish_BIT240269 d = new Dish_BIT240269();
        apply(d, form);
        return dishRepo.save(d);
    }

    public Dish_BIT240269 update(Long id, DishForm form) {
        Dish_BIT240269 d = getById(id);
        apply(d, form);
        return dishRepo.save(d);
    }

    private void apply(Dish_BIT240269 d, DishForm form) {
        DishCategory_BIT240269 category = categoryRepo.findById(form.getDishCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("error.category.notfound", form.getDishCategoryId()));
        d.setName(form.getName().trim());
        d.setPrice(form.getPrice());
        d.setPreparationTime(form.getPreparationTime());
        d.setIsAvailable(Boolean.TRUE.equals(form.getIsAvailable()));
        d.setDescription(form.getDescription());
        d.setCategory(category);
    }

    // ---------------- Ảnh món ăn ----------------

    /** Thêm ảnh bằng URL. Vi phạm quy tắc thì ném BusinessException (khóa thông báo i18n). */
    public void addImage(Long dishId, String url, boolean thumbnail) {
        Dish_BIT240269 dish = getById(dishId);
        if (url == null || url.isBlank()) throw new BusinessException("error.image.empty");
        url = url.trim();
        if (url.length() > 1000 || !URL_PATTERN.matcher(url).matches()) {
            throw new BusinessException("error.image.invalid");
        }
        if (thumbnail) {
            imageRepo.clearThumbnail(dishId);
        }
        DishImage_BIT240269 img = new DishImage_BIT240269();
        img.setImageUrl(url);
        img.setIsThumbnail(thumbnail);
        img.setDish(dish);
        imageRepo.save(img);
    }

    /** Chọn / đổi ảnh đại diện: ảnh cũ tự động thành ảnh thường. */
    public void setThumbnail(Long dishId, Long imageId) {
        getById(dishId);
        DishImage_BIT240269 img = getImage(dishId, imageId);
        imageRepo.clearThumbnail(dishId);
        // clearAutomatically đã làm mới persistence context nên nạp lại ảnh
        DishImage_BIT240269 fresh = imageRepo.findById(img.getId()).orElseThrow();
        fresh.setIsThumbnail(true);
        imageRepo.save(fresh);
    }

    public void deleteImage(Long dishId, Long imageId) {
        getById(dishId);
        imageRepo.delete(getImage(dishId, imageId));
    }

    private DishImage_BIT240269 getImage(Long dishId, Long imageId) {
        DishImage_BIT240269 img = imageRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("error.image.notfound", imageId));
        if (!img.getDish().getId().equals(dishId)) {
            throw new ResourceNotFoundException("error.image.notBelong", imageId);
        }
        return img;
    }
}
