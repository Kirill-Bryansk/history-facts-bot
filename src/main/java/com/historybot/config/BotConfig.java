package com.historybot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotConfig {

    private final Properties properties;

    public BotConfig() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки конфигурации: " + e.getMessage());
        }
    }

    public String getBotToken() {
        return properties.getProperty("bot.token", "");
    }

    public String getBotName() {
        return properties.getProperty("bot.username", "");
    }
}
