package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.UserState;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BackCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return command.equalsIgnoreCase("🔙 Назад") ||
               command.equalsIgnoreCase("Назад");
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        // СБРАСЫВАЕМ все флаги состояния
        context.getUserStateService().clearState(userId);
        context.getUserStateService().clearLastShownFact(userId);
        context.getUserStateService().clearSelectedCategory(userId);
        context.getUserStateService().clearUserChoseCategory(userId);

        System.out.println("DEBUG: Сброшены все флаги состояния для пользователя " + userId);

        // Возвращаем в главное меню
        context.getUserStateService().setUserState(userId, UserState.MAIN_MENU);

        context.getMessageService().sendMessageWithKeyboard(
                chatId, "🔙 Возвращаемся в главное меню:",
                KeyboardFactory.createMainKeyboard());
    }
}