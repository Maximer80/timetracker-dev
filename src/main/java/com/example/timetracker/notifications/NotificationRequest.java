package com.example.timetracker.notifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    private Long userId;
    private String message;
    private Notification.NotificationType type;
}
