package com.example.person_manager.repository;

import com.example.person_manager.model.Knife;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnifeRepository extends JpaRepository<Knife, Long> {
    List<Knife> findByManufacturerId(Long manufacturerId);

    List<Knife> findByCategoryId(Long categoryId);
    @Query("SELECT COUNT(k) > 0 FROM Knife k WHERE k.category.id = :categoryId")
    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT COUNT(k) > 0 FROM Knife k WHERE k.manufacturer.id = :manufacturerId")
    boolean existsByManufacturerId(Long manufacturerId);
}
