package com.example.timetracker.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Метод для создания уведомления
    public Notification createNotification(Long userId, String message, Notification.NotificationType type) {
        Notification notification = new Notification(userId, message, type);
        return notificationRepository.save(notification);
    }

    // Метод для получения всех уведомлений
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Метод для получения уведомлений конкретного пользователя
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    // Метод для отправки уведомлений (изменение статуса)
    public void sendNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        // Логика отправки уведомления (например, через почту, Telegram и т.д.)
        sendToTelegram(notification); // Пример функции отправки через Telegram

        notification.setSent(true); // Обновляем статус уведомления на "отправлено"
        notificationRepository.save(notification);
    }

    private void sendToTelegram(Notification notification) {
        // Заглушка для отправки уведомлений в Telegram
        System.out.println("Отправлено в Telegram: " + notification.getMessage());
    }
}
