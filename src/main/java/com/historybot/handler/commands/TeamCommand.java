package com.historybot.handler.commands;

import com.historybot.model.UserState;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TeamCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return command.equalsIgnoreCase("👥 Команда");
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();

        String message = "👥 *Наша команда историков:*\n\n" +
                         "🎨 Анна — искусство и культура\n" +
                         "🏺 Арина — древние цивилизации\n" +
                         "⚔️ Катя — средневековье\n" +
                         "🚀 Артемий — новейшая история\n\n" +
                         "💻 Кирилл — разработчик бота";

        context.getMessageService().sendMessage(chatId, message);
    }
}