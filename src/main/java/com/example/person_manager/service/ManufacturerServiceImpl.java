package com.example.person_manager.service;

import com.example.person_manager.model.Manufacturer;
import com.example.person_manager.repository.KnifeRepository;
import com.example.person_manager.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final KnifeRepository knifeRepository;

    @Autowired
    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, KnifeRepository knifeRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.knifeRepository = knifeRepository;
    }

    @Override
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public String deleteManufacturerById(Long id) {
        if (knifeRepository.existsByManufacturerId(id)) {
            return "Не удалось удалить производителя: существуют ножи, связанные с этим производителем.";
        }
        manufacturerRepository.deleteById(id);
        return "Производитель успешно удален.";
    }

    @Override
    public Manufacturer findById(Long id) {
        return manufacturerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerRepository.save(manufacturer);
    }

    @Override
    @Transactional
    public void updateManufacturer(Long id, Manufacturer manufacturerUpdate) {
        Manufacturer existingManufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Производитель не найден с id " + id));

        existingManufacturer.setName(manufacturerUpdate.getName());
        existingManufacturer.setCountry(manufacturerUpdate.getCountry());
        // Обновите другие поля, если они есть

        manufacturerRepository.save(existingManufacturer);
    }
}
