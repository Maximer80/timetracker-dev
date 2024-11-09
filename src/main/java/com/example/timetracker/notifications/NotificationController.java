package com.example.timetracker.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Создание нового уведомления
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(@RequestParam Long userId,
                                                           @RequestParam String message,
                                                           @RequestParam Notification.NotificationType type) {
        Notification notification = notificationService.createNotification(userId, message, type);
        return ResponseEntity.ok(notification);
    }

    // Получение всех уведомлений пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // Отправка уведомления
    @PostMapping("/send/{notificationId}")
    public ResponseEntity<String> sendNotification(@PathVariable Long notificationId) {
        notificationService.sendNotification(notificationId);
        return ResponseEntity.ok("Notification sent successfully");
    }
}
