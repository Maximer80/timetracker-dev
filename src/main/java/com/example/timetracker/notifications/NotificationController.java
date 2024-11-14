package com.example.timetracker.notifications;

import com.example.timetracker.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    // Создание нового уведомления
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationRequest request) {
        Notification notification = notificationService.createNotification(
                request.getUserId(),
                request.getMessage(),
                request.getType()
        );
        return ResponseEntity.ok(notification);
    }

    // Получение всех уведомлений для текущего пользователя
    @GetMapping("/user")
    public ResponseEntity<List<Notification>> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Long userId = userService.getUserIdByUsername(currentUsername);

        List<Notification> notifications;
        if (isAdmin(authentication)) {
            // Если администратор, возвращаем все уведомления
            notifications = notificationService.getAllNotifications();
        } else {
            // Если обычный пользователь, возвращаем только свои уведомления
            notifications = notificationService.getNotificationsByUser(userId);
        }

        return ResponseEntity.ok(notifications);
    }

    // Отправка уведомления
    @PostMapping("/send/{notificationId}")
    public ResponseEntity<String> sendNotification(@PathVariable Long notificationId) {
        notificationService.sendNotification(notificationId);
        return ResponseEntity.ok("Уведомление отправлено успешно");
    }

    // Метод для проверки, является ли текущий пользователь администратором
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("ADMIN"));
    }
}
