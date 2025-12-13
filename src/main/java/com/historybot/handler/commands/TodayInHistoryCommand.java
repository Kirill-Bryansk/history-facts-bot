package com.historybot.handler.commands;

import com.historybot.model.UserState;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TodayInHistoryCommand implements Command {

    @Override
    public boolean canHandle(String command, UserState userState) {
        return command.equalsIgnoreCase("📅 Сегодня в истории");
    }

    @Override
    public void handle(Update update, CommandContext context) {
        long chatId = update.getMessage().getChatId();

        context.getMessageService().sendMessage(chatId,
                "📅 *Сегодня в истории*\n\n" +
                "Календарь исторических событий пока в разработке.\n" +
                "Анна и Арина составляют список важных дат!");
    }
}