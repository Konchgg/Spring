package com.example.person_manager.service;

import com.example.person_manager.model.Category;
import com.example.person_manager.repository.CategoryRepository;
import com.example.person_manager.repository.KnifeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final KnifeRepository knifeRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, KnifeRepository knifeRepository) {
        this.categoryRepository = categoryRepository;
        this.knifeRepository = knifeRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void updateCategory(Long id, Category category) {
        if (categoryRepository.existsById(id)) {
            category.setId(id);
            categoryRepository.save(category);
        }
    }

    @Override
    public String deleteCategoryById(Long id) {
        if (knifeRepository.existsByCategoryId(id)) {
            return "Не удалось удалить категорию: существуют ножи, связанные с этой категорией.";
        }
        categoryRepository.deleteById(id);
        return "Категория успешно удалена.";
    }
}
