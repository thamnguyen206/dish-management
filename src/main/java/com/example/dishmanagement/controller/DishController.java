package com.example.dishmanagement.controller;

import com.example.dishmanagement.dto.DishFilter;
import com.example.dishmanagement.dto.DishForm;
import com.example.dishmanagement.entity.Dish_BIT240269;
import com.example.dishmanagement.exception.BusinessException;
import com.example.dishmanagement.service.DishCategoryService;
import com.example.dishmanagement.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;
    private final DishCategoryService categoryService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dishes";
    }

    // ---------- Danh sách + tìm kiếm / lọc / sắp xếp ----------
    @GetMapping("/dishes")
    public String index(@ModelAttribute("filter") DishFilter filter, BindingResult binding, Model model) {
        List<Dish_BIT240269> dishes = Collections.emptyList();
        String errorKey = null;

        if (binding.hasErrors()) {
            // giá nhập không phải số
            errorKey = "error.filter.price.invalid";
            filter.setMinPrice(null);
            filter.setMaxPrice(null);
        } else if ((filter.getMinPrice() != null && filter.getMinPrice().signum() < 0)
                || (filter.getMaxPrice() != null && filter.getMaxPrice().signum() < 0)
                || (filter.getMinPrice() != null && filter.getMaxPrice() != null
                    && filter.getMinPrice().compareTo(filter.getMaxPrice()) > 0)) {
            // khoảng giá không hợp lệ: số âm hoặc giá thấp > giá cao
            errorKey = "error.filter.price.range";
        } else {
            dishes = dishService.search(filter);
        }

        model.addAttribute("dishes", dishes);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("errorKey", errorKey);
        return "dish/index";
    }

    // ---------- Chi tiết ----------
    @GetMapping("/dishes/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("dish", dishService.getById(id));
        return "dish/detail";
    }

    // ---------- Create ----------
    @GetMapping("/dishes/create")
    public String createForm(Model model) {
        model.addAttribute("form", new DishForm());
        prepareForm(model, "dish.create.title", "/dishes/create");
        return "dish/form";
    }

    @PostMapping("/dishes/create")
    public String create(@Valid @ModelAttribute("form") DishForm form, BindingResult br,
                         Model model, RedirectAttributes ra) {
        dishService.validateBusiness(form, null, br);
        if (br.hasErrors()) {
            prepareForm(model, "dish.create.title", "/dishes/create");
            return "dish/form";
        }
        Dish_BIT240269 d = dishService.create(form);
        ra.addFlashAttribute("successKey", "msg.dish.created");
        return "redirect:/dishes/" + d.getId();
    }

    // ---------- Edit ----------
    @GetMapping("/dishes/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", dishService.toForm(dishService.getById(id)));
        prepareForm(model, "dish.edit.title", "/dishes/" + id + "/edit");
        return "dish/form";
    }

    @PostMapping("/dishes/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("form") DishForm form,
                       BindingResult br, Model model, RedirectAttributes ra) {
        dishService.getById(id); // ném 404 nếu không tồn tại
        dishService.validateBusiness(form, id, br);
        if (br.hasErrors()) {
            prepareForm(model, "dish.edit.title", "/dishes/" + id + "/edit");
            return "dish/form";
        }
        dishService.update(id, form);
        ra.addFlashAttribute("successKey", "msg.dish.updated");
        return "redirect:/dishes/" + id;
    }

    // ---------- Ảnh món ăn ----------
    @PostMapping("/dishes/{id}/images")
    public String addImage(@PathVariable Long id, @RequestParam("imageUrl") String imageUrl,
                           @RequestParam(value = "thumbnail", defaultValue = "false") boolean thumbnail,
                           RedirectAttributes ra) {
        try {
            dishService.addImage(id, imageUrl, thumbnail);
            ra.addFlashAttribute("successKey", "msg.image.added");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("errorKey", ex.getMessageKey());
        }
        return "redirect:/dishes/" + id;
    }

    @PostMapping("/dishes/{id}/images/{imageId}/thumbnail")
    public String setThumbnail(@PathVariable Long id, @PathVariable Long imageId, RedirectAttributes ra) {
        dishService.setThumbnail(id, imageId);
        ra.addFlashAttribute("successKey", "msg.image.thumbnail");
        return "redirect:/dishes/" + id;
    }

    @PostMapping("/dishes/{id}/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long id, @PathVariable Long imageId, RedirectAttributes ra) {
        dishService.deleteImage(id, imageId);
        ra.addFlashAttribute("successKey", "msg.image.deleted");
        return "redirect:/dishes/" + id;
    }

    private void prepareForm(Model model, String titleKey, String action) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("titleKey", titleKey);
        model.addAttribute("action", action);
    }
}
