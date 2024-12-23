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

    @GetMapping
    public String showKnives(Model model) {
        List<Knife> knives = knifeService.getAllKnives();
        model.addAttribute("knives", knives);
        return "Knife/knives";
    }

    @GetMapping("/create")
    public String createKnifeForm(Model model) {
        model.addAttribute("knife", new Knife());
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Knife/knife_form";
    }

    @PostMapping
    public String saveKnife(@ModelAttribute Knife knife) {
        knifeService.addKnife(knife);
        return "redirect:/knives";
    }

    @GetMapping("/edit/{id}")
    public String editKnifeForm(@PathVariable Long id, Model model) {
        Knife knife = knifeService.findById(id);
        if (knife == null) {
            model.addAttribute("errorMessage", "Нож не найден.");
            return "error";
        }
        model.addAttribute("knife", knife);
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Knife/knife_form";
    }

    @PostMapping("/update/{id}")
    public String updateKnife(@PathVariable Long id, @ModelAttribute Knife knife) {
        knifeService.updateKnife(id, knife);
        return "redirect:/knives";
    }

    @GetMapping("/details/{id}")
    public String knifeDetails(@PathVariable Long id, Model model) {
        Knife knife = knifeService.findById(id);
        if (knife == null) {
            model.addAttribute("errorMessage", "Нож не найден.");
            return "error";
        }
        List<Review> reviews = reviewService.getReviewsByKnifeId(id);
        model.addAttribute("knife", knife);
        model.addAttribute("reviews", reviews);
        model.addAttribute("newReview", new Review());
        return "Knife/knife_details";
    }

    @PostMapping("/details/{id}/addReview")
    public String addReview(@PathVariable Long id, @ModelAttribute("newReview") Review review) {
        Knife knife = knifeService.findById(id);
        if (knife != null) {
            review.setKnife(knife);
            review.setCreatedAt(LocalDateTime.now());
            reviewService.addReview(review);
        }
        return "redirect:/knives/details/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteKnife(@PathVariable Long id) {
        knifeService.deleteById(id);
        return "redirect:/knives";
    }

    @PostMapping("/details/{knifeId}/reviews/delete/{reviewId}")
    public String deleteReview(@PathVariable Long knifeId, @PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);
        return "redirect:/knives/details/" + knifeId;
    }
}
