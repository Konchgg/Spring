package com.example.person_manager.service;

import com.example.person_manager.model.Category;
import com.example.person_manager.repository.CategoryRepository;
import com.example.person_manager.repository.KnifeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Реализация сервиса для управления категориями
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final KnifeRepository knifeRepository;

    // Конструктор с использованием @Autowired для внедрения зависимостей
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, KnifeRepository knifeRepository) {
        this.categoryRepository = categoryRepository;
        this.knifeRepository = knifeRepository;
    }

    // Получение всех категорий
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Добавление новой категории
    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    // Поиск категории по ID
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Обновление существующей категории по ID
    @Override
    public void updateCategory(Long id, Category category) {
        if (categoryRepository.existsById(id)) {
            category.setId(id); // Установка ID для категории, чтобы обновление прошло успешно
            categoryRepository.save(category);
        }
    }

    // Удаление категории по ID с проверкой наличия связанных ножей
    @Override
    public String deleteCategoryById(Long id) {
        if (knifeRepository.existsByCategoryId(id)) {
            return "Не удалось удалить категорию: существуют ножи, связанные с этой категорией.";
        }
        categoryRepository.deleteById(id);
        return "Категория успешно удалена.";
    }
}
