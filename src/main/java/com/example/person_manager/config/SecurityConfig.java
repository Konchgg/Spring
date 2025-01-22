package com.example.person_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;


@Configuration
@EnableMethodSecurity // Включаем возможность использовать аннотации @PreAuthorize при желании
public class SecurityConfig {

    /**
     * 1. Настраиваем пользователей в памяти (InMemoryUserDetailsManager).
     *    - Один пользователь будет с логином "admin" и ролью "ADMIN"
     *    - Второй пользователь - с логином "user" и ролью "USER"
     *
     * 2. Пароли сохраняем в зашифрованном виде (BCryptPasswordEncoder).
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123")) // пароль - "admin123"
                .roles("ADMIN") // даём роль ADMIN
                .build();

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))   // пароль - "user123"
                .roles("USER")  // даём роль USER
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    /**
     * Вспомогательный бином, который отвечает за шифрование пароля.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Основная настройка безопасности приложения:
     * - /knives/create/**, /knives/edit/**, /knives/delete/**, /categories/**, /manufacturers/**, /orders/**
     *   доступны только пользователю с ролью ADMIN.
     * - Все остальные запросы (cart, просмотр ножей и т.д.) доступны всем (или авторизованным пользователям, если захотим).
     *
     * - Включаем formLogin() – стандартная форма входа Spring Security.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/knives/create/**", "/knives/edit/**", "/knives/delete/**").hasRole("ADMIN")
                        .requestMatchers("/categories/**", "/manufacturers/**").hasRole("ADMIN")
                        //.requestMatchers("/orders/**").hasRole("ADMIN")
                        // Открываем доступ к странице логина, CSS, JS и т.п.:
                        .requestMatchers("/auth/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // Остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Кастомная страница входа:
                        .loginPage("/auth/login")
                        // URL, на который отправляется форма (совпадает с action="/login" в шаблоне):
                        .loginProcessingUrl("/login")
                        // Куда перенаправлять после успешной аутентификации:
                        .defaultSuccessUrl("/knives", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")                       // URL для выхода
                        .logoutSuccessUrl("/knives")     // Куда перенаправить после выхода
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}