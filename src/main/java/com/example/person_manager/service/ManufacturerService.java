package com.example.person_manager.service;

import com.example.person_manager.model.Manufacturer;
import java.util.List;

// Интерфейс для управления операциями, связанными с производителями
public interface ManufacturerService {

    // Получение списка всех производителей
    List<Manufacturer> getAllManufacturers();

    // Удаление производителя по ID
    String deleteManufacturerById(Long id);

    // Поиск производителя по ID
    Manufacturer findById(Long id);

    // Добавление нового производителя
    void addManufacturer(Manufacturer manufacturer);

    // Обновление данных производителя по ID
    void updateManufacturer(Long id, Manufacturer manufacturer);
}
