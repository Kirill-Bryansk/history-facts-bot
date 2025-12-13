package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.util.FactFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

public class FavoriteSelectionCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        // Работает только в состоянии FAVORITES и если команда - число
        if (userState != UserState.FAVORITES) return false;

        try {
            int num = Integer.parseInt(command.trim());
            return num > 0; // Номер должен быть положительным
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        String command = update.getMessage().getText().trim();

        try {
            int factNumber = Integer.parseInt(command);

            // Получаем список избранного
            List<Fact> favorites = context.getFavoriteService().getUserFavorites(userId);

            if (factNumber > 0 && factNumber <= favorites.size()) {
                Fact fact = favorites.get(factNumber - 1);

                // Показываем факт с возможностью удаления
                context.getUserStateService().setLastShownFact(userId, fact.getId());
                context.getUserStateService().setUserState(userId, UserState.VIEWING_FACT);

                String message = FactFormatter.formatFactMessage(fact);
                boolean isFavorite = true; // В избранном точно есть

                context.getMessageService().sendMessageWithKeyboard(
                        chatId, message + "\n\n📌 *Этот факт в вашем избранном*",
                        KeyboardFactory.createFactActionsKeyboard(isFavorite));

            } else {
                context.getMessageService().sendMessage(chatId,
                        "⚠️ Неверный номер. У вас " + favorites.size() + " фактов в избранном.");
            }

        } catch (NumberFormatException e) {
            // Не должно произойти, т.к. canHandle проверяет
        }
    }
}