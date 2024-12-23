package com.example.person_manager.service;

import com.example.person_manager.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void addCategory(Category category); // Новый метод
    Category findById(Long id); // Новый метод
    void updateCategory(Long id, Category category); // Новый метод
    String deleteCategoryById(Long id);
}
