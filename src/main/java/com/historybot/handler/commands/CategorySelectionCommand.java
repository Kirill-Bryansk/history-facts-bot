package com.historybot.handler.commands;

import com.historybot.database.DatabaseConfig;
import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.util.FactFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategorySelectionCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        // Эта команда работает только когда пользователь в состоянии CATEGORIES
        return userState == UserState.CATEGORIES;
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        String categoryName = update.getMessage().getText();

        // ОТЛАДКА: выводим что получили
        System.out.println("=== DEBUG CategorySelection ===");
        System.out.println("Полученный текст: '" + categoryName + "'");

        // Убираем эмодзи из названия категории
        String cleanCategoryName = categoryName.replaceAll("^[^\\p{L}\\p{N}]+\\s*", "");
        System.out.println("Очищенное название: '" + cleanCategoryName + "'");

        // Если нажали "Назад" в меню категорий
        if (cleanCategoryName.equalsIgnoreCase("Назад")) {
            new BackCommand().handle(update, context);
            return;
        }

        // ДОПОЛНИТЕЛЬНАЯ ОТЛАДКА: выводим все категории из базы
        System.out.println("Категории в базе фактов:");
        List<String> allCategories = getAllCategoriesFromDB(context);
        for (String cat : allCategories) {
            System.out.println("  - '" + cat + "'");
        }

        // Ищем факт по выбранной категории
        Fact fact = context.getFactService().getRandomFactByCategory(cleanCategoryName);

        if (fact != null) {
            System.out.println("Найден факт для категории: " + cleanCategoryName);
            showFact(chatId, userId, fact, context);
        } else {
            System.out.println("Факты для категории '" + cleanCategoryName + "' не найдены");
            context.getMessageService().sendMessage(chatId,
                    "😔 В категории \"" + cleanCategoryName + "\" пока нет фактов.\nПопробуйте другую категорию.");
        }
    }

    // Добавь этот метод для отладки
    private List<String> getAllCategoriesFromDB(CommandContext context) {
        List<String> categories = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT DISTINCT category FROM facts ORDER BY category");
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения категорий из БД: " + e.getMessage());
        }
        return categories;
    }

    private void showFact(long chatId, Long userId, Fact fact, CommandContext context) {
        if (fact != null) {
            // ПОЛЬЗОВАТЕЛЬ ВЫБРАЛ КАТЕГОРИЮ
            context.getUserStateService().setUserChoseCategory(userId, true);

            // Сохраняем выбранную категорию
            context.getUserStateService().setSelectedCategory(userId, fact.getCategory());

            // Увеличиваем просмотры
            context.getFactService().incrementViews(fact.getId());

            // СОХРАНЯЕМ ID показанного факта
            context.getUserStateService().setLastShownFact(userId, fact.getId());

            // Устанавливаем состояние
            context.getUserStateService().setUserState(userId, UserState.VIEWING_FACT);

            // Форматируем сообщение
            String message = FactFormatter.formatFactMessage(fact);

            // Проверяем избранное
            boolean isFavorite = context.getFavoriteService().isFavorite(userId, fact.getId());

            // Отправляем с клавиатурой
            context.getMessageService().sendMessageWithKeyboard(
                    chatId, message,
                    KeyboardFactory.createFactActionsKeyboard(isFavorite));
        }
    }
}