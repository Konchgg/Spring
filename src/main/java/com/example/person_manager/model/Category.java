package com.example.person_manager.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories") // Указание имени таблицы в базе данных
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID
    private Long id;

    @Column(unique = true, nullable = false) // Уникальное и не null имя категории
    private String name;

    @OneToMany(mappedBy = "category") // Соотношение с сущностью Knife (один ко многим)
    private List<Knife> knives; // Список ножей, относящихся к данной категории

    // Геттеры и сеттеры для доступа к полям

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Knife> getKnives() {
        return knives;
    }

    public void setKnives(List<Knife> knives) {
        this.knives = knives;
    }
}
