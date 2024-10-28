package com.example.timetracker.time_tracking;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_sessions")
public class WorkSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // Конструкторы
    public WorkSession() {
    }

    public WorkSession(Long userId, LocalDateTime startTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.status = Status.IN_PROGRESS; // Устанавливаем начальный статус как "в процессе"
    }

    // Enum для статусов
    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }

    // Методы для завершения и продолжения сессии
    public void completeSession() {
        this.endTime = LocalDateTime.now();
        this.status = Status.COMPLETED;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}