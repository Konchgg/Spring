package com.example.person_manager.service;

import com.example.person_manager.model.Knife;
import com.example.person_manager.repository.KnifeRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KnifeServiceImpl implements KnifeService {

    private final KnifeRepository knifeRepository;

    public KnifeServiceImpl(KnifeRepository knifeRepository) {
        this.knifeRepository = knifeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Knife> getAllKnives() {
        return knifeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Knife findById(Long id) {
        return knifeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        knifeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addKnife(Knife knife) {
        knifeRepository.save(knife);
    }

    @Override
    @Transactional
    public void updateKnife(Long id, Knife knifeUpdate) {
        Knife existingKnife = knifeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knife not found with id " + id));

        existingKnife.setName(knifeUpdate.getName());
        existingKnife.setPrice(knifeUpdate.getPrice());
        existingKnife.setQuantity(knifeUpdate.getQuantity());
        existingKnife.setManufacturer(knifeUpdate.getManufacturer());
        existingKnife.setCategory(knifeUpdate.getCategory());
        existingKnife.setDescription(knifeUpdate.getDescription());
        existingKnife.setManufactureDate(knifeUpdate.getManufactureDate());

        knifeRepository.save(existingKnife);
    }

    @Override
    @Transactional
    public void updateKnifeQuantity(Long id, int delta) {
        Knife knife = knifeRepository.findById(id).orElse(null);
        if (knife != null) {
            int newQuantity = knife.getQuantity() + delta;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Недостаточно ножей на складе.");
            } else if (newQuantity == 0) {
                knifeRepository.deleteById(id);
            } else {
                knife.setQuantity(newQuantity);
                knifeRepository.save(knife);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByManufacturerId(Long manufacturerId) {
        return knifeRepository.findByManufacturerId(manufacturerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByCategoryId(Long categoryId) {
        return knifeRepository.findByCategoryId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByName(String name) {
        return knifeRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByCategoryIdAndName(Long categoryId, String name) {
        return knifeRepository.findByCategoryIdAndName(categoryId, name);
    }
}

