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
            String text = update.getMessage().getText();

            // Обработка команды /start
            if (text.equals("/start")) {
                sendMessage(chatId, "Привет! Добро пожаловать в TimeTracker.");
            }
            // Обработка другой команды или текста
            else if (text.equals("hello")) {
                sendMessage(chatId, "Привет! Как я могу помочь?");
            }
            else {
                sendMessage(chatId, "Привет! Ваш запрос: " + text);
            }
        }
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
