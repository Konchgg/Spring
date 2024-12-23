package com.example.person_manager.service;

import com.example.person_manager.model.Review;
import com.example.person_manager.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsByKnifeId(Long knifeId) {
        return reviewRepository.findByKnifeId(knifeId);
    }

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}