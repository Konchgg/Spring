package com.example.person_manager.repository;

import com.example.person_manager.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Поиск отзывов по ID ножа
    List<Review> findByKnifeId(Long knifeId);
}