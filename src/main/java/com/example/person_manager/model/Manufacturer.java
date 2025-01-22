package com.example.person_manager.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "manufacturers") // Указание имени таблицы производителей в базе данных
public class Manufacturer {

    @Id // Аннотация для первичного ключа
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID
    private Long id;

    @Column(unique = true, nullable = false) // Поле имени производителя, должно быть уникальным и не null
    private String name;

    @Column(nullable = true) // Поле для страны производителя, может быть пустым
    private String country;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Связь один ко многим с каскадированием
    private List<Knife> knives; // Список ножей, производимых этим производителем

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Knife> getKnives() {
        return knives;
    }

    public void setKnives(List<Knife> knives) {
        this.knives = knives;
    }
}
