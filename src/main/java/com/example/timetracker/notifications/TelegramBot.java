package com.example.timetracker.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Profile("!test") // Отключить бин для тестов
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

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
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText().trim();

            switch (text.toLowerCase()) {
                case "/start":
                    sendMessage(chatId, "Привет! Добро пожаловать в timetracker.");
                    break;
                case "/start_session":
                    startWorkSession(chatId);
                    break;
                case "/end_session":
                    endWorkSession(chatId);
                    break;
                case "/stats":
                    showStats(chatId);
                    break;
                case "/help":
                    sendHelp(chatId);
                    break;
                default:
                    sendMessage(chatId, "Привет! Ваш запрос: " + text);
            }
        }
    }

    private void startWorkSession(String chatId) {
        // Логика для запуска сессии
        sendMessage(chatId, "Вы начали рабочую сессию.");
    }

    private void endWorkSession(String chatId) {
        // Логика для завершения сессии
        sendMessage(chatId, "Вы завершили рабочую сессию.");
    }

    private void showStats(String chatId) {
        // Логика для отображения статистики
        sendMessage(chatId, "Ваша статистика за сегодня: 4 часа.");
    }

    private void sendHelp(String chatId) {
        String helpText = """
        /start - Начать работу с ботом.
        /start_session - Начать рабочую сессию.
        /end_session - Завершить рабочую сессию.
        /stats - Показать статистику.
        /help - Список доступных команд.
        """;
        sendMessage(chatId, helpText);
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        // Вызов метода отправки сообщения
        try {
            execute(message);  // Может вызвать TelegramApiException
        } catch (TelegramApiException e) {
            // Логируем ошибку с подробностями
            logger.error("Ошибка при отправке сообщения пользователю с chatId {}: {}", chatId, e.getMessage(), e);
        }
    }
}
