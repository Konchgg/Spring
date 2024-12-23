package com.example.person_manager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "knife_id", nullable = false)
    private Knife knife;

    @Column(name = "review_text", nullable = false)
    private String reviewText;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Конструктор по умолчанию
    public Review() {}

    // Getters и Setters
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
