package com.example.timetracker.time_tracking;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_sessions")
@Getter
@Setter
public class WorkSession {

    // Публичный конструктор по умолчанию
    public WorkSession() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd | HH:mm:ss")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd | HH:mm:ss")
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // Enum для статусов
    public enum Status {
        IN_PROGRESS,
        COMPLETED
    }

    // Конструктор для создания новой сессии (всегда IN_PROGRESS)
    public WorkSession(Long userId, LocalDateTime startTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.status = Status.IN_PROGRESS; // Начальный статус "в процессе"
    }

    // Метод завершения сессии
    public void completeSession() {
        if (this.status == Status.IN_PROGRESS) {
            this.endTime = LocalDateTime.now();
            this.status = Status.COMPLETED;
        }
    }

    // Проверка, является ли сессия активной
    public boolean isActive() {
        return this.status == Status.IN_PROGRESS;
    }
}
