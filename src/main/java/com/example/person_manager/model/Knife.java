package com.example.person_manager.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "knives") // Указание имени таблицы в базе данных
public class Knife {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID
    private Long id;

    @Column(nullable = false) // Поле имени ножа, не может быть пустым
    private String name;

    @Column(nullable = false) // Поле цены ножа, не может быть пустым
    private BigDecimal price;

    @Column(nullable = false) // Поле количества ножей, не может быть пустым
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false) // Внешний ключ на производителя
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // Внешний ключ на категорию
    private Category category;

    private String description; // Описание ножа

    @Column(name = "manufacture_date") // Дата производства ножа
    private LocalDate manufactureDate;

    // Связь один ко многим с сущностью Review с каскадированием и удалением сирот
    @OneToMany(mappedBy = "knife", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        // Удаляем текущие рецензии
        this.reviews.forEach(review -> review.setKnife(null));
        this.reviews.clear();
        // Устанавливаем новые рецензии
        reviews.forEach(this::addReview);
    }

    // Добавление рецензии к ножу
    public void addReview(Review review) {
        reviews.add(review);
        review.setKnife(this);
    }

    // Удаление рецензии
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setKnife(null);
    }
}
