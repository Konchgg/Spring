package com.example.person_manager.controller;

import com.example.person_manager.model.Category;
import com.example.person_manager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/category_list";
    }

    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/category_form";
    }

    @PostMapping("/create")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            model.addAttribute("errorMessage", "Категория не найдена.");
            return "error";
        }
        model.addAttribute("category", category);
        return "category/category_form";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        categoryService.updateCategory(id, category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model) {
        String message = categoryService.deleteCategoryById(id);
        if (message.contains("не удалось")) {
            model.addAttribute("errorMessage", message);
            return "error";
        }
        return "redirect:/categories";
    }
}