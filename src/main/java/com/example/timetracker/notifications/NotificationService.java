package com.example.timetracker.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TelegramBot telegramBot; // Telegram бот для отправки сообщений
    private final JavaMailSender mailSender; // Сервис для отправки e-mail

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               TelegramBot telegramBot,
                               JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.telegramBot = telegramBot;
        this.mailSender = mailSender;
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

    // Метод для отправки уведомления через указанный канал
    public void sendNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (notification.getType() == Notification.NotificationType.TELEGRAM) {
            sendToTelegram(notification.getUserId().toString(), notification.getMessage());
        } else if (notification.getType() == Notification.NotificationType.EMAIL) {
            sendToEmail(notification.getUserId().toString() + "@example.com",
                    "Уведомление",
                    notification.getMessage());
        }

        notification.setSent(true); // Обновляем статус уведомления на "отправлено"
        notificationRepository.save(notification);
    }

    // Метод для отправки уведомления в Telegram
    private void sendToTelegram(String chatId, String message) {
        telegramBot.sendMessage(chatId, message);
    }

    // Метод для отправки уведомления по e-mail
    private void sendToEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("your-email@example.com");
        mailSender.send(email);
    }
}
