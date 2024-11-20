package com.example.timetracker.notifications;

import com.example.timetracker.telegram_integration.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TelegramBot telegramBot; // Добавляем TelegramBot
    private final JavaMailSender mailSender; // Добавляем JavaMailSender для e-mail

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

    // Метод для отправки уведомлений (изменение статуса)
    public void sendNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        // Логика отправки уведомления (например, через Telegram)
        sendToTelegram(notification.getUserId().toString(), notification.getMessage());

        notification.setSent(true); // Обновляем статус уведомления на "отправлено"
        notificationRepository.save(notification);
    }

    // Метод для отправки напоминания
    public void sendReminder(Long chatId, String message) {
        System.out.println("Напоминание отправлено пользователю с ID: " + chatId);
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
