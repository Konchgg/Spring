package com.example.person_manager.service;

import com.example.person_manager.model.Review;
import com.example.person_manager.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Сервис для управления отзывами
@Service
public class ReviewService {

    // Репозиторий отзывов
    private final ReviewRepository reviewRepository;

    // Конструктор с внедрением зависимостей
    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Получение списка отзывов по ID ножа
    public List<Review> getReviewsByKnifeId(Long knifeId) {
        return reviewRepository.findByKnifeId(knifeId);
    }

    // Добавление нового отзыва
    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    // Удаление отзыва по ID
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
