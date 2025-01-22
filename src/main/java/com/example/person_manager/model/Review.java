package com.example.person_manager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews") // Указание имени таблицы отзывов в базе данных
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Связь многие к одному с сущностью Knife
    @JoinColumn(name = "knife_id", nullable = false) // Внешний ключ на нож
    private Knife knife;

    @Column(name = "review_text", nullable = false) // Текст отзыва, обязательное поле
    private String reviewText;

    @Column(name = "created_at", nullable = false) // Дата создания отзыва, обязательное поле
    private LocalDateTime createdAt = LocalDateTime.now(); // Инициализация текущей датой и временем

    // Конструктор по умолчанию
    public Review() {}

    // Геттеры и сеттеры для доступа к полям

    public Long getId() {
        return id;
    }

    public Knife getKnife() {
        return knife;
    }

    public void setKnife(Knife knife) {
        this.knife = knife;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
