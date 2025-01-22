package com.example.person_manager.service;

import com.example.person_manager.model.Category;
import java.util.List;

// Интерфейс для управления бизнес-логикой, связанной с категориями
public interface CategoryService {

    // Получение списка всех категорий
    List<Category> getAllCategories();

    // Добавление новой категории
    void addCategory(Category category);

    // Поиск категории по ID
    Category findById(Long id);

    // Обновление существующей категории по ID
    void updateCategory(Long id, Category category);

    // Удаление категории по ID
    String deleteCategoryById(Long id);
}
