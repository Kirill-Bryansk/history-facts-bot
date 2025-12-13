package com.historybot;

import com.historybot.bot.HistoryFactsBot;
import com.historybot.database.DatabaseConfig;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        // Инициализируем базу данных
        DatabaseConfig.initDatabase();
        System.out.println("База данных инициализирована");

        // Запускаем бота
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new HistoryFactsBot());
            System.out.println("Бот успешно запущен!");
        } catch (TelegramApiException e) {
            System.err.println("Ошибка запуска бота: " + e.getMessage());
        }
    }
}