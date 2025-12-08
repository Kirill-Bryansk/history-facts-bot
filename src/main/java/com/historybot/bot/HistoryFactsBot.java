package com.historybot.bot;

import com.historybot.config.BotConfig;
import com.historybot.handler.CommandHandler;
import com.historybot.service.MessageService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HistoryFactsBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CommandHandler commandHandler;

    public HistoryFactsBot() {
        this.botConfig = new BotConfig();
        MessageService messageService = new MessageService(this);
        this.commandHandler = new CommandHandler(messageService);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        commandHandler.handleUpdate(update);
    }
}