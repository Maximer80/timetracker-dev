package com.example.timetracker.notifications;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot();
    }
}
