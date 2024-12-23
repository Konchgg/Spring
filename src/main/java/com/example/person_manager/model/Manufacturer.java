package com.example.person_manager.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "manufacturers")
public class Manufacturer {

    @Id // Используйте аннотацию Id из jakarta.persistence
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = true)
    private String country;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Knife> knives;

    // Getters и Setters

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