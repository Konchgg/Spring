package com.example.person_manager.controller;

import com.example.person_manager.model.Manufacturer;
import com.example.person_manager.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public String listManufacturers(Model model) {
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        return "manufacturer/manufacturer_list";
    }

    @GetMapping("/create")
    public String createManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "manufacturer/manufacturer_form";
    }

    @PostMapping("/create")
    public String addManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.addManufacturer(manufacturer);
        return "redirect:/manufacturers";
    }

    @GetMapping("/edit/{id}")
    public String editManufacturerForm(@PathVariable Long id, Model model) {
        Manufacturer manufacturer = manufacturerService.findById(id);
        if (manufacturer == null) {
            model.addAttribute("errorMessage", "Производитель не найден.");
            return "error";
        }
        model.addAttribute("manufacturer", manufacturer);
        return "manufacturer/manufacturer_form";
    }

    @PostMapping("/edit/{id}")
    public String updateManufacturer(@PathVariable Long id, @ModelAttribute Manufacturer manufacturer) {
        manufacturerService.updateManufacturer(id, manufacturer);
        return "redirect:/manufacturers";
    }

    @GetMapping("/delete/{id}")
    public String deleteManufacturer(@PathVariable Long id, Model model) {
        String message = manufacturerService.deleteManufacturerById(id);
        if (message.contains("не удалось")) {
            model.addAttribute("errorMessage", message);
            return "error";
        }
        return "redirect:/manufacturers";
    }
}