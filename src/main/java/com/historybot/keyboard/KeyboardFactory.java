package com.historybot.keyboard;

import com.historybot.model.Category;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    // Основная клавиатура
    public static ReplyKeyboardMarkup createMainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первый ряд кнопок
        KeyboardRow row1 = new KeyboardRow();
        row1.add("📜 Случайный факт");
        row1.add("🏷️ Категории");

        // Второй ряд кнопок
        KeyboardRow row2 = new KeyboardRow();
        row2.add("📅 Сегодня в истории");
        row2.add("👥 Команда");

        // Третий ряд кнопок
        KeyboardRow row3 = new KeyboardRow();
        row3.add("⭐ Избранное");
        row3.add("🔙 Назад");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    // Клавиатура с категориями
    public static ReplyKeyboardMarkup createCategoriesKeyboard(List<Category> categories) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Добавляем категории по 2 в ряд
        KeyboardRow currentRow = null;
        for (int i = 0; i < categories.size(); i++) {
            if (i % 2 == 0) {
                currentRow = new KeyboardRow();
                keyboard.add(currentRow);
            }

            Category category = categories.get(i);
            String buttonText = category.getEmoji() + " " + category.getName();
            currentRow.add(buttonText);
        }

        // Добавляем кнопку "Назад" в отдельный ряд
        KeyboardRow backRow = new KeyboardRow();
        backRow.add("🔙 Назад");
        keyboard.add(backRow);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    // Клавиатура для избранного (ИЗМЕНИЛ private НА public)
    public static ReplyKeyboardMarkup createFavoritesKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📜 Случайный факт");
        row1.add("🏷️ Категории");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("🔙 Назад");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    // Клавиатура для действий с фактом
    public static ReplyKeyboardMarkup createFactActionsKeyboard(boolean isFavorite) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📜 Ещё факт");
        row1.add(isFavorite ? "❌ Удалить из избранного" : "⭐ В избранное");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("🏷️ Категории");
        row2.add("🔙 Назад");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}