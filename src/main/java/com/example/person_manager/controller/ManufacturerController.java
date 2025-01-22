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

    // Получение списка всех производителей
    @GetMapping
    public String listManufacturers(Model model) {
        model.addAttribute("manufacturers", manufacturerService.getAllManufacturers());
        return "manufacturer/manufacturer_list"; // Возвращаем вид для отображения списка производителей
    }

    // Форма для создания нового производителя
    @GetMapping("/create")
    public String createManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer()); // Добавляем пустого производителя для формы
        return "manufacturer/manufacturer_form"; // Возвращаем вид формы для производителя
    }

    // Добавление нового производителя
    @PostMapping("/create")
    public String addManufacturer(@ModelAttribute Manufacturer manufacturer) {
        manufacturerService.addManufacturer(manufacturer); // Сохраняем нового производителя
        return "redirect:/manufacturers"; // Перенаправление на список производителей
    }

    // Форма для редактирования существующего производителя
    @GetMapping("/edit/{id}")
    public String editManufacturerForm(@PathVariable Long id, Model model) {
        Manufacturer manufacturer = manufacturerService.findById(id);
        if (manufacturer == null) {
            model.addAttribute("errorMessage", "Производитель не найден.");
            return "error"; // Возвращаем вид ошибки, если производитель не найден
        }
        model.addAttribute("manufacturer", manufacturer); // Добавляем существующего производителя в модель
        return "manufacturer/manufacturer_form"; // Возвращаем вид формы для производителя
    }

    // Обновление информации о производителе
    @PostMapping("/edit/{id}")
    public String updateManufacturer(@PathVariable Long id, @ModelAttribute Manufacturer manufacturer) {
        manufacturerService.updateManufacturer(id, manufacturer); // Обновляем данные производителя
        return "redirect:/manufacturers"; // Перенаправление на список производителей
    }

    // Удаление производителя
    @GetMapping("/delete/{id}")
    public String deleteManufacturer(@PathVariable Long id, Model model) {
        String message = manufacturerService.deleteManufacturerById(id); // Пытаемся удалить производителя
        if (message.contains("не удалось")) {
            model.addAttribute("errorMessage", message);
            return "error"; // Возвращаем вид ошибки, если удаление не удалось
        }
        return "redirect:/manufacturers"; // Перенаправление на список производителей
    }
}
