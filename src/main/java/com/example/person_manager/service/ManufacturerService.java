package com.example.person_manager.service;

import com.example.person_manager.model.Manufacturer;
import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> getAllManufacturers();
    String deleteManufacturerById(Long id);
    Manufacturer findById(Long id);
    void addManufacturer(Manufacturer manufacturer);
    void updateManufacturer(Long id, Manufacturer manufacturer);
}