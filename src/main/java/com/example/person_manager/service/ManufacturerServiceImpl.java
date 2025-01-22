package com.example.person_manager.service;

import com.example.person_manager.model.Manufacturer;
import com.example.person_manager.repository.KnifeRepository;
import com.example.person_manager.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Реализация сервиса для управления производителями
@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final KnifeRepository knifeRepository;

    // Конструктор для внедрения репозиториев зависимостей
    @Autowired
    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, KnifeRepository knifeRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.knifeRepository = knifeRepository;
    }

    // Получение списка всех производителей
    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    // Удаление производителя по ID с проверкой наличия связанных ножей
    @Override
    public String deleteManufacturerById(Long id) {
        if (knifeRepository.existsByManufacturerId(id)) {
            return "Не удалось удалить производителя: существуют ножи, связанные с этим производителем.";
        }
        manufacturerRepository.deleteById(id);
        return "Производитель успешно удален.";
    }

    // Поиск производителя по ID
    @Override
    public Manufacturer findById(Long id) {
        return manufacturerRepository.findById(id).orElse(null);
    }

    // Добавление нового производителя в базу данных
    @Override
    @Transactional
    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerRepository.save(manufacturer);
    }

    // Обновление информации о производителе
    @Override
    @Transactional
    public void updateManufacturer(Long id, Manufacturer manufacturerUpdate) {
        Manufacturer existingManufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Производитель не найден с id " + id));

        // Обновление полей производителя
        existingManufacturer.setName(manufacturerUpdate.getName());
        existingManufacturer.setCountry(manufacturerUpdate.getCountry());
        // Обновите другие поля, если они есть

        // Сохранение обновленной информации
        manufacturerRepository.save(existingManufacturer);
    }
}
