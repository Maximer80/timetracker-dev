package com.example.timetracker.telegram_integration;

import com.example.timetracker.time_tracking.WorkSessionService;
import com.example.timetracker.notifications.NotificationService;
import com.example.timetracker.reporting.ReportingService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final WorkSessionService workSessionService;
    private final NotificationService notificationService;
    private final ReportingService reportingService;

    public TelegramBot(WorkSessionService workSessionService,
                       NotificationService notificationService,
                       ReportingService reportingService) {
        this.workSessionService = workSessionService;
        this.notificationService = notificationService;
        this.reportingService = reportingService;
    }

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start_session":
                    startWorkSession(chatId);
                    break;
                case "/stop_session":
                    stopWorkSession(chatId);
                    break;
                case "/stats":
                    sendUserStats(chatId);
                    break;
                case "/reminder":
                    sendReminder(chatId);
                    break;
                case "/help":
                    sendHelp(chatId);
                    break;
                default:
                    sendMessage(chatId, "Команда не распознана.");
            }
        }
    }

    private void startWorkSession(Long chatId) {
        try {
            workSessionService.startSession(chatId);
            sendMessage(chatId, "Рабочая сессия успешно начата!");
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка при запуске рабочей сессии: " + e.getMessage());
        }
    }

    private void stopWorkSession(Long chatId) {
        try {
            workSessionService.endSession(chatId);
            sendMessage(chatId, "Рабочая сессия успешно завершена!");
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка при завершении рабочей сессии: " + e.getMessage());
        }
    }

    private void sendUserStats(Long chatId) {
        try {
            String stats = workSessionService.getBasicStats(chatId);
            sendMessage(chatId, "Ваша статистика:\n" + stats);
        } catch (Exception e) {
            sendMessage(chatId, "Не удалось получить статистику: " + e.getMessage());
        }
    }

    private void sendReminder(Long chatId) {
        notificationService.sendReminder(chatId, "Не забудьте продолжить работу!");
        sendMessage(chatId, "Напоминание отправлено!");
    }

    private void sendHelp(Long chatId) {
        String helpText = """
                /start_session - Начать рабочую сессию.
                /stop_session - Завершить рабочую сессию.
                /stats - Показать статистику.
                /reminder - Отправить напоминание.
                /help - Список доступных команд.
                """;
        sendMessage(chatId, helpText);
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId.toString(), text);
    }
}
