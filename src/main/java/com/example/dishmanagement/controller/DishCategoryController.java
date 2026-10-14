package com.example.dishmanagement.controller;

import com.example.dishmanagement.dto.DishCategoryForm;
import com.example.dishmanagement.exception.BusinessException;
import com.example.dishmanagement.service.DishCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class DishCategoryController {

    private final DishCategoryService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("categories", service.findAll());
        return "category/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("categoryForm", new DishCategoryForm());
        prepare(model, "category.create.title", "/categories/create");
        return "category/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("categoryForm") DishCategoryForm form, BindingResult br,
                         Model model, RedirectAttributes ra) {
        service.validateBusiness(form, null, br);
        if (br.hasErrors()) {
            prepare(model, "category.create.title", "/categories/create");
            return "category/form";
        }
        service.save(null, form);
        ra.addFlashAttribute("successKey", "msg.category.saved");
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("categoryForm", service.toForm(service.getById(id)));
        prepare(model, "category.edit.title", "/categories/" + id + "/edit");
        return "category/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute("categoryForm") DishCategoryForm form,
                       BindingResult br, Model model, RedirectAttributes ra) {
        service.getById(id); // 404 nếu không tồn tại
        service.validateBusiness(form, id, br);
        if (br.hasErrors()) {
            prepare(model, "category.edit.title", "/categories/" + id + "/edit");
            return "category/form";
        }
        service.save(id, form);
        ra.addFlashAttribute("successKey", "msg.category.saved");
        return "redirect:/categories";
    }

    /** Ràng buộc: không xóa loại món nếu còn món thuộc loại này */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("successKey", "msg.category.deleted");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("errorKey", ex.getMessageKey());
            ra.addFlashAttribute("errorArg", ex.getArg());
        }
        return "redirect:/categories";
    }

    private void prepare(Model model, String titleKey, String action) {
        model.addAttribute("titleKey", titleKey);
        model.addAttribute("action", action);
    }
}
