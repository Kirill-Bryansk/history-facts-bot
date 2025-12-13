/*
package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.util.FactFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FactActionsCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return userState == UserState.VIEWING_FACT && (
                command.equalsIgnoreCase("⭐ В избранное") ||
                command.equalsIgnoreCase("❌ Удалить из избранного") ||
                command.equalsIgnoreCase("🏷️ Категории") ||
                command.equalsIgnoreCase("📜 Ещё факт"));
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        String action = update.getMessage().getText();

        // Получаем последний показанный факт
        Long factId = context.getUserStateService().getLastShownFact(userId);

        if (factId == null) {
            context.getMessageService().sendMessage(chatId,
                    "Что-то пошло не так. Попробуйте выбрать факт заново.");
            return;
        }

        if (action.equalsIgnoreCase("📜 Ещё факт")) {
            new RandomFactCommand().handle(update, context);

        } else if (action.equalsIgnoreCase("🏷️ Категории")) {
            new CategoriesCommand().handle(update, context);

        } else if (action.equalsIgnoreCase("⭐ В избранное") ||
                   action.equalsIgnoreCase("❌ Удалить из избранного")) {

            handleFavoriteAction(chatId, userId, factId, action, context);
        }
    }

    private void handleFavoriteAction(long chatId, Long userId, Long factId,
                                      String action, CommandContext context) {
        boolean isFavorite = context.getFavoriteService().isFavorite(userId, factId);

        if (action.contains("❌")) { // Удалить из избранного
            boolean removed = context.getFavoriteService().removeFromFavorites(userId, factId);
            if (removed) {
                context.getMessageService().sendMessage(chatId, "❌ Удалено из избранного.");
            }
        } else { // Добавить в избранное
            boolean added = context.getFavoriteService().addToFavorites(userId, factId);
            if (added) {
                context.getMessageService().sendMessage(chatId, "✅ Добавлено в избранное!");
            } else {
                context.getMessageService().sendMessage(chatId, "⚠️ Уже в избранном.");
            }
        }

        // Показываем обновленный факт
        showFact(chatId, userId, factId, context);
    }

    private void showFact(long chatId, Long userId, Long factId, CommandContext context) {
        Fact fact = context.getFactService().getFactById(factId);
        if (fact != null) {
            boolean isFavorite = context.getFavoriteService().isFavorite(userId, factId);
            String message = FactFormatter.formatFactMessage(fact);
            context.getMessageService().sendMessageWithKeyboard(
                    chatId, message, KeyboardFactory.createFactActionsKeyboard(isFavorite));
        }
    }
}*/
