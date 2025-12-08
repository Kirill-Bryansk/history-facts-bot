package com.historybot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboardMarkup createMainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первый ряд кнопок
        KeyboardRow row1 = new KeyboardRow();
        row1.add("📜 Факт");
        row1.add("📅 Сегодня в истории");

        // Второй ряд кнопок
        KeyboardRow row2 = new KeyboardRow();
        row2.add("🏷️ Категории");
        row2.add("👥 Команда");

        // Третий ряд кнопок
        KeyboardRow row3 = new KeyboardRow();
        row3.add("ℹ️ О боте");
        row3.add("⭐ Избранное");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}