package com.example.person_manager.service;

import com.example.person_manager.model.Knife;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface KnifeService {
    List<Knife> getAllKnives();
    Knife findById(Long id);
    void deleteById(Long id);
    void addKnife(Knife knife);

    @Transactional
    void updateKnife(Long id, Knife knifeUpdate);

    void updateKnifeQuantity(Long id, int delta);

    List<Knife> findKnivesByManufacturerId(Long manufacturerId);

    List<Knife> findKnivesByCategoryId(Long categoryId);
}
