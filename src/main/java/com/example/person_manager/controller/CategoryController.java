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

    // Получение списка всех категорий
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/category_list"; // Возвращаем вид для отображения списка категорий
    }

    // Форма для создания новой категории
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category()); // Добавляем пустую категорию для формы
        return "category/category_form"; // Возвращаем вид формы категории
    }

    // Обработка добавления новой категории
    @PostMapping("/create")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category); // Сохраняем новую категорию
        return "redirect:/categories"; // Перенаправление на список категорий
    }

    // Форма для редактирования существующей категории
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        if (category == null) {
            model.addAttribute("errorMessage", "Категория не найдена.");
            return "error"; // Возвращаем вид ошибки, если категория не найдена
        }
        model.addAttribute("category", category); // Добавляем существующую категорию в модель
        return "category/category_form"; // Возвращаем вид формы категории
    }

    // Обработка обновления категории
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        categoryService.updateCategory(id, category); // Обновляем категорию
        return "redirect:/categories"; // Перенаправление на список категорий
    }

    // Удаление категории
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model) {
        String message = categoryService.deleteCategoryById(id); // Пытаемся удалить категорию
        if (message.contains("не удалось")) {
            model.addAttribute("errorMessage", message);
            return "error"; // Возвращаем вид ошибки, если удаление не удалось
        }
        return "redirect:/categories"; // Перенаправление на список категорий
    }
}
