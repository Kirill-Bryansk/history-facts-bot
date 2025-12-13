package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.util.FactFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FavoriteToggleCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return userState == UserState.VIEWING_FACT && (
                command.equalsIgnoreCase("⭐ В избранное") ||
                command.equalsIgnoreCase("❌ Удалить из избранного"));
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        Long factId = context.getUserStateService().getLastShownFact(userId);
        if (factId == null) return;

        boolean isFavorite = context.getFavoriteService().isFavorite(userId, factId);

        if (isFavorite) {
            context.getFavoriteService().removeFromFavorites(userId, factId);
            context.getMessageService().sendMessage(chatId, "❌ Удалено из избранного.");
        } else {
            context.getFavoriteService().addToFavorites(userId, factId);
            context.getMessageService().sendMessage(chatId, "✅ Добавлено в избранное!");
        }

        // Показываем обновленный факт
        showUpdatedFact(chatId, userId, factId, context);
    }

    private void showUpdatedFact(long chatId, Long userId, Long factId, CommandContext context) {
        Fact fact = context.getFactService().getFactById(factId);
        if (fact != null) {
            boolean isFavorite = context.getFavoriteService().isFavorite(userId, factId);
            String message = FactFormatter.formatFactMessage(fact);
            context.getMessageService().sendMessageWithKeyboard(
                    chatId, message, KeyboardFactory.createFactActionsKeyboard(isFavorite));
        }
    }
}