package com.example.person_manager.controller;

import com.example.person_manager.model.Knife;
import com.example.person_manager.model.Review;
import com.example.person_manager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/knives")
public class KnifeController {

    private final KnifeService knifeService;
    private final ReviewService reviewService;
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;

    @Autowired
    public KnifeController(KnifeService knifeService, ReviewService reviewService,
                           ManufacturerService manufacturerService, CategoryService categoryService) {
        this.knifeService = knifeService;
        this.reviewService = reviewService;
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
    }

    // Показать все ножи с возможностью фильтрации по категории и поиску
    @GetMapping
    public String showKnives(@RequestParam(required = false) Long categoryId,
                             @RequestParam(required = false) String search,
                             Model model) {
        List<Knife> knives;

        // Логика фильтрации и поиска
        if (categoryId != null && search != null && !search.isEmpty()) {
            knives = knifeService.findKnivesByCategoryIdAndName(categoryId, search);
        } else if (categoryId != null) {
            knives = knifeService.findKnivesByCategoryId(categoryId);
        } else if (search != null && !search.isEmpty()) {
            knives = knifeService.findKnivesByName(search);
        } else {
            knives = knifeService.getAllKnives();
        }

        // Добавление данных в модель
        model.addAttribute("knives", knives);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("searchTerm", search);
        return "Knife/knives"; // Возвращение шаблона с ножами
    }

    // Форма для создания нового ножа
    @GetMapping("/create")
    public String createKnifeForm(Model model) {
        model.addAttribute("knife", new Knife());
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Knife/knife_form"; // Возвращение шаблона формы ножа
    }

    // Сохранение нового ножа
    @PostMapping
    public String saveKnife(@ModelAttribute Knife knife) {
        knifeService.addKnife(knife);
        return "redirect:/knives"; // Перенаправление на список ножей
    }

    // Форма для редактирования существующего ножа
    @GetMapping("/edit/{id}")
    public String editKnifeForm(@PathVariable Long id, Model model) {
        Knife knife = knifeService.findById(id);
        if (knife == null) {
            model.addAttribute("errorMessage", "Нож не найден.");
            return "error"; // Возврат ошибки, если нож не найден
        }
        model.addAttribute("knife", knife);
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Knife/knife_form"; // Возвращение шаблона формы ножа
    }

    // Обновление существующего ножа
    @PostMapping("/update/{id}")
    public String updateKnife(@PathVariable Long id, @ModelAttribute Knife knife) {
        knifeService.updateKnife(id, knife);
        return "redirect:/knives"; // Перенаправление на список ножей
    }

    // Подробности о ноже, включая отзывы
    @GetMapping("/details/{id}")
    public String knifeDetails(@PathVariable Long id, Model model) {
        Knife knife = knifeService.findById(id);
        if (knife == null) {
            model.addAttribute("errorMessage", "Нож не найден.");
            return "error"; // Возврат ошибки, если нож не найден
        }
        List<Review> reviews = reviewService.getReviewsByKnifeId(id);
        model.addAttribute("knife", knife);
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new Review());
        return "Knife/knife_details"; // Возвращение шаблона с деталями и отзывами ножа
    }

    // Добавление отзыва к ножу
    @PostMapping("/details/{id}/addReview")
    public String addReview(@PathVariable Long id, @ModelAttribute("newReview") Review review) {
        Knife knife = knifeService.findById(id);
        if (knife != null) {
            review.setKnife(knife);
            review.setCreatedAt(LocalDateTime.now());
            reviewService.addReview(review);
        }
        return "redirect:/knives/details/" + id; // Перенаправление на страницу с деталями ножа
    }

    // Удаление ножа
    @GetMapping("/delete/{id}")
    public String deleteKnife(@PathVariable Long id) {
        knifeService.deleteById(id);
        return "redirect:/knives"; // Перенаправление на список ножей
    }

    // Удаление отзыва о ноже
    @PostMapping("/details/{knifeId}/reviews/delete/{reviewId}")
    public String deleteReview(@PathVariable Long knifeId, @PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);
        return "redirect:/knives/details/" + knifeId; // Перенаправление на страницу с деталями ножа
    }
}
