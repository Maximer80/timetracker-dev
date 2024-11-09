package com.example.timetracker.notifications;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String message;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean sent;

    // Конструкторы

    public Notification() {}

    public Notification(Long userId, String message, NotificationType type) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.sent = false;
    }

    // Enum для типов уведомлений
    public enum NotificationType {
        SESSION_START,
        SESSION_END,
        REMINDER
    }
}
