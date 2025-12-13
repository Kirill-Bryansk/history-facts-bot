package com.historybot.handler;

import com.historybot.handler.commands.Command;
import com.historybot.handler.commands.CommandContext;
import com.historybot.handler.commands.CommandFactory;
import com.historybot.model.UserState;
import com.historybot.service.*;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler {

    private final CommandFactory commandFactory;
    private final MessageService messageService;
    private final UserStateService userStateService;

    public CommandHandler(MessageService messageService) {
        this.messageService = messageService;

        // Создаем сервисы
        FactService factService = new FactService();
        CategoryService categoryService = new CategoryService();
        UserService userService = new UserService();
        FavoriteService favoriteService = new FavoriteService();
        this.userStateService = new UserStateService();

        // Создаем контекст
        CommandContext context = new CommandContext(
                messageService, factService, categoryService,
                userService, favoriteService, userStateService
        );

        // Создаем фабрику команд
        this.commandFactory = new CommandFactory(context);
    }

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long userId = update.getMessage().getFrom().getId();

            // Получаем состояние пользователя
            UserState currentState = userStateService.getUserState(userId);

            // Находим подходящую команду
            Command command = commandFactory.getCommand(messageText, currentState);

            if (command != null) {
                command.handle(update, commandFactory.getContext());
            } else {
                // Обработка неизвестной команды
                handleUnknownCommand(update);
            }
        }
    }

    private void handleUnknownCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        messageService.sendMessage(
                chatId, "Используйте кнопки ниже или напишите /start");
    }
}