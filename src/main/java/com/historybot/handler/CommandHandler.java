package com.historybot.handler;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.service.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler {

    private final MessageService messageService;

    public CommandHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            handleCommand(chatId, messageText);
        }
    }

    private void handleCommand(long chatId, String command) {
        if (command.equals("/start") || command.equalsIgnoreCase("Вперед")) {
            messageService.sendMessageWithKeyboard(chatId,
                    "👋 Приветствую, команда историков-энтузиастов!\n\n*От Кирилла с любовью к истории для:*\n✨ Анны\n✨ Арины\n✨ Кати\n✨ Артемия\n\nВыберите действие:",
                    KeyboardFactory.createMainKeyboard());
        } else if (command.equalsIgnoreCase("Факт") || command.equalsIgnoreCase("📜 Факт")) {
            messageService.sendMessage(chatId,
                    "⏳ *Факто-генератор в разработке*\n\nСейчас наша команда историков:\n• Анна проверяет " +
                    "достоверность фактов\n• Арина ищет античные источники\n• Катя сверяет даты в летописях\n• " +
                    "Артемий готовит аналитику\n\nА Кирилл пишет код, чтобы объединить " +
                    "все это в одном боте!\n\nОбещаю, скоро здесь появятся настоящие исторические сокровища! 🏺");
        } else if (command.equalsIgnoreCase("О боте") || command.equalsIgnoreCase("ℹ️ О боте")) {
            messageService.sendMessage(chatId, "🤖 *History Facts Bot*\n\nСоздатель: Кирилл\nДля:" +
                                               " Анны, Арины, Кати, Артемия\n\nВерсия: 1.0 (в разработке)\n\nЭтот бот " +
                                               "будет собирать интересные исторические факты по разным категориям.");
        } else if (command.equalsIgnoreCase("Команда") || command.equalsIgnoreCase("👥 Команда")) {
            messageService.sendMessage(chatId, "👥 *Наша команда историков:*\n\n🎨 Анна — искусство и " +
                                               "культура\n🏺 Арина — древние цивилизации\n⚔️ Катя — средневековье\n🚀 " +
                                               "Артемий — новейшая история\n\n💻 Кирилл — разработчик бота");
        } else if (command.equalsIgnoreCase("📅 Сегодня в истории")) {
            messageService.sendMessage(chatId, "📅 *Сегодня в истории*\n\nКалендарь исторических событий " +
                                               "пока в разработке. Анна и Арина составляют список важных дат!");
        } else if (command.equalsIgnoreCase("🏷️ Категории")) {
            messageService.sendMessage(chatId, "🏷️ *Категории фактов*\n\n1. Древний мир 🏺\n2. " +
                                               "Средневековье ⚔️\n3. Новое время 🎨\n4. Новейшая история 🚀\n5." +
                                               "Россия 🇷🇺\n\nВыберите категорию (скоро будет доступно)");
        } else if (command.equalsIgnoreCase("⭐ Избранное")) {
            messageService.sendMessage(chatId, "⭐ *Избранное*\n\nЗдесь будут сохраняться " +
                                               "понравившиеся факты. Пока функция в разработке!");
        } else {
            messageService.sendMessage(chatId, "Используйте кнопки ниже или напишите /start или вперед");
        }
    }
}