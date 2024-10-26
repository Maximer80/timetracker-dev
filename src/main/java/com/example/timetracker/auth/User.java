package com.example.timetracker.auth;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Задаем имя таблицы PostgreSQL
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false) // Указываем, что поле не может быть null
    private String role; // Добавляем поле role
    
    // Конструктор без параметров
    public User() {
    }
    
    // Конструктор с параметрами
    public User(String username, String password, String role) { // Обновляем конструктор
        this.username = username;
        this.password = password;
        this.role = role; // Устанавливаем роль
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() { // Геттер для role
    	return role;
    }
    
    public void setRole(String role) { // Сеттер для role
    	this.role = role;
    }
    
    @Override
    public String toString() {
    	return "User{" +
    			"id=" + id +
    			", username='" + username + '\'' +
    			'}';
    }
}
