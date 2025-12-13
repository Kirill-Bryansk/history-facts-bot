package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.util.FactFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MoreFromCategoryCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        // ТОЛЬКО когда в состоянии VIEWING_FACT и нажали "Ещё факт"
        return userState == UserState.VIEWING_FACT &&
               command.equalsIgnoreCase("📜 Ещё факт");
    }

    @Override
    public void handle(Update update, CommandContext context) {
        System.out.println("=== MoreFromCategoryCommand triggered ===");

        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        // ПРОВЕРЯЕМ - пользователь сам выбрал категорию?
        boolean userChoseCategory = context.getUserStateService().didUserChooseCategory(userId);
        System.out.println("Пользователь выбирал категорию? " + userChoseCategory);

        if (!userChoseCategory) {
            // Если пользователь НЕ выбирал категорию - показываем случайный факт
            System.out.println("Пользователь не выбирал категорию - показываем случайный факт");
            new RandomFactCommand().handle(update, context);
            return;
        }

        // Получаем последний показанный факт
        Long lastFactId = context.getUserStateService().getLastShownFact(userId);
        if (lastFactId == null) {
            new RandomFactCommand().handle(update, context);
            return;
        }

        // Получаем категорию последнего факта
        Fact lastFact = context.getFactService().getFactById(lastFactId);
        if (lastFact == null) {
            new RandomFactCommand().handle(update, context);
            return;
        }

        String category = lastFact.getCategory();
        System.out.println("Ищем еще факт из категории: " + category);

        // Ищем ДРУГОЙ факт из той же категории
        Fact newFact = getAnotherFactFromCategory(category, lastFactId, context);

        if (newFact != null) {
            showFact(chatId, userId, newFact, context);
        } else {
            context.getMessageService().sendMessage(chatId,
                    "😔 В категории \"" + category + "\" больше нет других фактов.");
            showFact(chatId, userId, lastFact, context);
        }
    }

    private Fact getAnotherFactFromCategory(String category, Long excludeFactId, CommandContext context) {
        // Ищем факт из той же категории, но не тот же самый
        String sql = "SELECT * FROM facts WHERE category = ? AND id != ? AND verified = 1 ORDER BY RANDOM() LIMIT 1";

        try (var conn = com.historybot.database.DatabaseConfig.getConnection();
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);
            pstmt.setLong(2, excludeFactId);
            var rs = pstmt.executeQuery();

            if (rs.next()) {
                return com.historybot.database.DatabaseUtils.mapResultSetToFact(rs);
            }
        } catch (Exception e) {
            System.err.println("Ошибка поиска другого факта: " + e.getMessage());
        }
        return null;
    }

    private void showFact(long chatId, Long userId, Fact fact, CommandContext context) {
        if (fact != null) {
            context.getFactService().incrementViews(fact.getId());
            context.getUserStateService().setLastShownFact(userId, fact.getId());
            context.getUserStateService().setUserState(userId, UserState.VIEWING_FACT);

            // СОХРАНЯЕМ ФЛАГ что пользователь выбрал категорию
            context.getUserStateService().setUserChoseCategory(userId, true);

            String message = FactFormatter.formatFactMessage(fact);
            boolean isFavorite = context.getFavoriteService().isFavorite(userId, fact.getId());

            context.getMessageService().sendMessageWithKeyboard(
                    chatId, message, KeyboardFactory.createFactActionsKeyboard(isFavorite));
        }
    }
}