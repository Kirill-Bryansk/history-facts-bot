package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.util.FactFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

public class FavoritesCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return command.equalsIgnoreCase("⭐ Избранное") ||
               command.equalsIgnoreCase("Избранное");
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        // Получаем избранные факты
        List<Fact> favorites = context.getFavoriteService().getUserFavorites(userId);
        int count = context.getFavoriteService().getFavoritesCount(userId);

        if (!favorites.isEmpty()) {
            // Устанавливаем состояние
            context.getUserStateService().setUserState(userId, UserState.FAVORITES);

            // Формируем сообщение
            StringBuilder message = new StringBuilder();
            message.append("⭐ *Ваше избранное*\n\n");
            message.append("Всего сохранено фактов: ").append(count).append("\n\n");

            // Показываем первые 5 фактов
            int limit = Math.min(5, favorites.size());
            for (int i = 0; i < limit; i++) {
                Fact fact = favorites.get(i);
                message.append(i + 1).append(". ").append(fact.getContent()).append("\n");
                message.append("   🏷️ ").append(fact.getCategory());
                if (fact.getYear() != 0) {
                    message.append(" | 🗓️ ").append(fact.getYear());
                }
                message.append("\n\n");
            }

            if (favorites.size() > 5) {
                message.append("... и еще ").append(favorites.size() - 5).append(" фактов\n\n");
            }

            message.append("Используйте кнопки для навигации:");

            // Отправляем с клавиатурой для избранного
            context.getMessageService().sendMessageWithKeyboard(
                    chatId, message.toString(), KeyboardFactory.createFavoritesKeyboard());

        } else {
            context.getMessageService().sendMessage(chatId,
                    "⭐ *Ваше избранное*\n\n" +
                    "У вас пока нет сохраненных фактов.\n" +
                    "Чтобы добавить факт в избранное, нажмите кнопку \"⭐ В избранное\" при просмотре факта.");
        }
    }
}