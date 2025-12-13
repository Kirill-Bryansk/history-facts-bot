package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.UserState;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return command.equals("/start") ||
               command.equalsIgnoreCase("Вперёд");
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        // Регистрируем пользователя
        context.getUserService().getOrCreateUser(update.getMessage());

        // Устанавливаем состояние
        context.getUserStateService().setUserState(userId, UserState.MAIN_MENU);

        // Отправляем приветствие
        String message = "👋 Приветствую!\n\nВыберите действие:";

        context.getMessageService().sendMessageWithKeyboard(
                chatId, message, KeyboardFactory.createMainKeyboard());
    }
}