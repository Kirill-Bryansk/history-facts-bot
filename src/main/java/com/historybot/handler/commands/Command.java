package com.historybot.handler.commands;

import com.historybot.model.UserState;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    boolean canHandle(String command, UserState userState);
    void handle(Update update, CommandContext context);
}