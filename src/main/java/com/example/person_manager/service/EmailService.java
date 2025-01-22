package com.example.person_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Сервис для отправки простых текстовых email-сообщений
@Service
public class EmailService {

    private final JavaMailSender emailSender;

    // Адрес отправителя, извлекаемая из конфигурации
    @Value("${spring.mail.username}")
    private String from;

    // Конструктор с использованием @Autowired для внедрения зависимостей
    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    // Метод для отправки простого email-сообщения
    public void sendSimpleEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from); // Установка адреса отправителя
        simpleMailMessage.setTo(toAddress); // Установка адреса получателя
        simpleMailMessage.setSubject(subject); // Установка темы сообщения
        simpleMailMessage.setText(message); // Установка текста сообщения
        emailSender.send(simpleMailMessage); // Отправка сообщения
    }
}
