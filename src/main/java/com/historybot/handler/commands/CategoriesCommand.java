package com.historybot.handler.commands;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Category;
import com.historybot.model.UserState;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

public class CategoriesCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return command.equalsIgnoreCase("🏷️ Категории") ||
               command.equalsIgnoreCase("Категории");
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        // Получаем список категорий
        List<Category> categories = context.getCategoryService().getAllCategories();

        if (!categories.isEmpty()) {
            // Устанавливаем состояние
            context.getUserStateService().setUserState(userId, UserState.CATEGORIES);

            // Формируем сообщение
            StringBuilder message = new StringBuilder("🏷️ *Выберите категорию:*\n\n");
            for (Category category : categories) {
                message.append(category.getEmoji())
                        .append(" *")
                        .append(category.getName())
                        .append("* - ")
                        .append(category.getDescription())
                        .append("\n\n");
            }

            // Отправляем с клавиатурой категорий
            context.getMessageService().sendMessageWithKeyboard(
                    chatId, message.toString(),
                    KeyboardFactory.createCategoriesKeyboard(categories));
        } else {
            context.getMessageService().sendMessage(
                    chatId, "Категории пока не добавлены.");
        }
    }
}