package com.example.person_manager.service;

import com.example.person_manager.model.Knife;
import com.example.person_manager.repository.KnifeRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Сервисный класс для управления операциями с ножами
@Service
public class KnifeServiceImpl implements KnifeService {

    private final KnifeRepository knifeRepository;

    // Конструктор для внедрения зависимостей
    public KnifeServiceImpl(KnifeRepository knifeRepository) {
        this.knifeRepository = knifeRepository;
    }

    // Получение всех ножей с аннотацией readOnly для оптимизации производительности
    @Override
    @Transactional(readOnly = true)
    public List<Knife> getAllKnives() {
        return knifeRepository.findAll();
    }

    // Поиск ножа по ID, возвращает null, если не найдено
    @Override
    @Transactional(readOnly = true)
    public Knife findById(Long id) {
        return knifeRepository.findById(id).orElse(null);
    }

    // Удаление ножа по ID
    @Override
    @Transactional
    public void deleteById(Long id) {
        knifeRepository.deleteById(id);
    }

    // Добавление нового ножа
    @Override
    @Transactional
    public void addKnife(Knife knife) {
        knifeRepository.save(knife);
    }

    // Обновление информации о ноже
    @Override
    @Transactional
    public void updateKnife(Long id, Knife knifeUpdate) {
        // Поиск существующего ножа или выброс исключения
        Knife existingKnife = knifeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knife not found with id " + id));

        // Обновление полей сущности
        existingKnife.setName(knifeUpdate.getName());
        existingKnife.setPrice(knifeUpdate.getPrice());
        existingKnife.setQuantity(knifeUpdate.getQuantity());
        existingKnife.setManufacturer(knifeUpdate.getManufacturer());
        existingKnife.setCategory(knifeUpdate.getCategory());
        existingKnife.setDescription(knifeUpdate.getDescription());
        existingKnife.setManufactureDate(knifeUpdate.getManufactureDate());

        // Сохранение обновленной информации
        knifeRepository.save(existingKnife);
    }

    // Обновление количества ножей
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

    // Поиск ножей по ID производителя
    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByManufacturerId(Long manufacturerId) {
        return knifeRepository.findByManufacturerId(manufacturerId);
    }

    // Поиск ножей по ID категории
    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByCategoryId(Long categoryId) {
        return knifeRepository.findByCategoryId(categoryId);
    }

    // Поиск ножей по имени (независимо от регистра)
    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByName(String name) {
        return knifeRepository.findByNameContainingIgnoreCase(name);
    }

    // Поиск ножей по ID категории и имени
    @Override
    @Transactional(readOnly = true)
    public List<Knife> findKnivesByCategoryIdAndName(Long categoryId, String name) {
        return knifeRepository.findByCategoryIdAndName(categoryId, name);
    }
}
