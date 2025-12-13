package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.util.FactFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class RandomFactCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        // Срабатывает ТОЛЬКО в главном меню
        return (userState == UserState.MAIN_MENU || userState == null) &&
               (command.equalsIgnoreCase("📜 Случайный факт") ||
                command.equalsIgnoreCase("Случайный факт"));
    }

    @Override
    public void handle(Update update, CommandContext context) {
        System.out.println("=== RandomFactCommand (из главного меню) ===");

        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        // Получаем случайный факт
        Fact fact = context.getFactService().getRandomFact();
        showFact(chatId, userId, fact, context);
    }

    private void showFact(long chatId, Long userId, Fact fact, CommandContext context) {
        if (fact != null) {
            // Увеличиваем просмотры
            context.getFactService().incrementViews(fact.getId());

            // Устанавливаем состояние
            context.getUserStateService().setUserState(userId, UserState.VIEWING_FACT);

            // Сохраняем ID показанного факта
            context.getUserStateService().setLastShownFact(userId, fact.getId());

            // СБРАСЫВАЕМ флаг "пользователь выбрал категорию"
            context.getUserStateService().setUserChoseCategory(userId, false);
            System.out.println("DEBUG: Сброшен флаг userChoseCategory для пользователя " + userId);

            // Форматируем сообщение
            String message = formatFactMessage(fact);

            // Проверяем избранное
            boolean isFavorite = context.getFavoriteService().isFavorite(userId, fact.getId());

            // Создаем клавиатуру
            ReplyKeyboardMarkup keyboard = createFactActionsKeyboard(isFavorite);

            // Отправляем
            context.getMessageService().sendMessageWithKeyboard(chatId, message, keyboard);
        } else {
            context.getMessageService().sendMessage(
                    chatId, "📭 Пока нет фактов в базе. Скоро добавлю!");
        }
    }

    private String formatFactMessage(Fact fact) {
        return FactFormatter.formatFactMessage(fact);
    }

    private ReplyKeyboardMarkup createFactActionsKeyboard(boolean isFavorite) {
        return KeyboardFactory.createFactActionsKeyboard(isFavorite);
    }
}