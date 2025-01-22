package com.example.person_manager.repository;

import com.example.person_manager.model.Knife;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnifeRepository extends JpaRepository<Knife, Long> {

    // Поиск ножей по ID производителя
    List<Knife> findByManufacturerId(Long manufacturerId);

    // Поиск ножей по ID категории
    List<Knife> findByCategoryId(Long categoryId);

    // Поиск ножей по частичному совпадению имени (без учета регистра)
    List<Knife> findByNameContainingIgnoreCase(String name);

    // Поиск ножей по ID категории и частичному совпадению имени (без учета регистра)
    @Query("SELECT k FROM Knife k WHERE k.category.id = :categoryId AND LOWER(k.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Knife> findByCategoryIdAndName(Long categoryId, String name);

    // Проверка существования ножей по ID категории
    @Query("SELECT COUNT(k) > 0 FROM Knife k WHERE k.category.id = :categoryId")
    boolean existsByCategoryId(Long categoryId);

    // Проверка существования ножей по ID производителя
    @Query("SELECT COUNT(k) > 0 FROM Knife k WHERE k.manufacturer.id = :manufacturerId")
    boolean existsByManufacturerId(Long manufacturerId);
}
