package com.example.person_manager.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PersonDTO {
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Отчество обязательно")
    private String patronymic;

    @NotBlank(message = "Пол обязателен")
    private String gender;

    @NotBlank(message = "Национальность обязательна")
    private String nationality;

    @Min(value = 50, message = "Рост должен быть не менее 50 см")
    @Max(value = 300, message = "Рост не может превышать 300 см")
    private int height;

    @Min(value = 20, message = "Вес должен быть не менее 20 кг")
    @Max(value = 600, message = "Вес не может превышать 600 кг")
    private int weight;

    @NotNull(message = "Дата рождения обязательна")
    private String birthDate;

    @NotBlank(message = "Номер телефона обязателен")
    private String phoneNumber;

    private AddressDTO address;

    // Геттеры
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getGender() {
        return gender;
    }

    public String getNationality() {
        return nationality;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AddressDTO getAddress() {
        return address;
    }

    // Сеттеры
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
