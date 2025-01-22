package com.example.person_manager.service;

import com.example.person_manager.model.Knife;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Интерфейс для управления операциями сервиса, связанными с ножами
public interface KnifeService {

    // Получение списка всех ножей
    List<Knife> getAllKnives();

    // Поиск ножа по ID
    Knife findById(Long id);

    // Удаление ножа по ID
    void deleteById(Long id);

    // Добавление нового ножа
    void addKnife(Knife knife);

    // Обновление данных ножа по ID с использованием транзакции
    @Transactional
    void updateKnife(Long id, Knife knifeUpdate);

    // Обновление количества ножей по ID
    void updateKnifeQuantity(Long id, int delta);

    // Поиск ножей по ID производителя
    List<Knife> findKnivesByManufacturerId(Long manufacturerId);

    // Поиск ножей по ID категории
    List<Knife> findKnivesByCategoryId(Long categoryId);

    // Поиск ножей по имени
    List<Knife> findKnivesByName(String name);

    // Поиск ножей по ID категории и имени
    List<Knife> findKnivesByCategoryIdAndName(Long categoryId, String name);
}
