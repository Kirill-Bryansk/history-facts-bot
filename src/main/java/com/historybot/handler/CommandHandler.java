package com.historybot.handler;

import com.historybot.keyboard.KeyboardFactory;
import com.historybot.model.Category;
import com.historybot.model.Fact;
import com.historybot.model.UserState;
import com.historybot.service.CategoryService;
import com.historybot.service.FactService;
import com.historybot.service.MessageService;
import com.historybot.service.UserStateService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class CommandHandler {

    private final MessageService messageService;
    private final FactService factService;
    private final CategoryService categoryService;
    private final UserStateService userStateService;

    public CommandHandler(MessageService messageService) {
        this.messageService = messageService;
        this.factService = new FactService();
        this.categoryService = new CategoryService();
        this.userStateService = new UserStateService();
    }

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Long userId = update.getMessage().getFrom().getId();

            // Получаем текущее состояние пользователя
            UserState currentState = userStateService.getUserState(userId);

            // Обрабатываем команду в зависимости от состояния
            switch (currentState) {
                case CATEGORIES:
                    handleCategorySelection(chatId, userId, messageText);
                    break;
                default:
                    handleCommand(chatId, userId, messageText);
                    break;
            }
        }
    }

    private void handleCategorySelection(long chatId, Long userId, String categoryName) {
        // Убираем эмодзи из названия категории (если есть)
        String cleanCategoryName = categoryName.replaceFirst("^[^\\p{L}\\p{N}]\\s*", "");

        if (cleanCategoryName.equalsIgnoreCase("Назад")) {
            // Возврат в главное меню
            userStateService.setUserState(userId, UserState.MAIN_MENU);
            messageService.sendMessageWithKeyboard(chatId,
                    "🔙 Возвращаемся в главное меню:",
                    KeyboardFactory.createMainKeyboard());
            return;
        }

        // Ищем факт по выбранной категории
        Fact fact = factService.getRandomFactByCategory(cleanCategoryName);

        if (fact != null) {
            // Сохраняем выбранную категорию
            userStateService.setSelectedCategory(userId, cleanCategoryName);
            userStateService.setUserState(userId, UserState.VIEWING_FACT);

            // Отправляем факт
            String factMessage = formatFactMessage(fact);
            messageService.sendMessageWithKeyboard(chatId, factMessage,
                    KeyboardFactory.createFactActionsKeyboard());
        } else {
            messageService.sendMessage(chatId,
                    "😔 В категории \"" + cleanCategoryName + "\" пока нет фактов.\nПопробуйте другую категорию.");
        }
    }

    private void handleCommand(long chatId, Long userId, String command) {
        if (command.equals("/start") || command.equalsIgnoreCase("Вперед")) {
            userStateService.setUserState(userId, UserState.MAIN_MENU);
            messageService.sendMessageWithKeyboard(chatId,
                    "👋 Приветствую, команда историков-энтузиастов!\n\n*От Кирилла с любовью к истории для:*\n✨ Анны\n✨ Арины\n✨ Кати\n✨ Артемия\n\nВыберите действие:",
                    KeyboardFactory.createMainKeyboard());

        } else if (command.equalsIgnoreCase("📜 Случайный факт") ||
                   command.equalsIgnoreCase("Случайный факт") ||
                   command.equalsIgnoreCase("📜 Ещё факт")) {

            handleRandomFact(chatId, userId);

        } else if (command.equalsIgnoreCase("🏷️ Категории") ||
                   command.equalsIgnoreCase("Категории")) {

            showCategories(chatId, userId);

        } else if (command.equalsIgnoreCase("👥 Команда")) {
            messageService.sendMessage(chatId, "👥 *Наша команда историков:*\n\n🎨 Анна — искусство и культура\n🏺 Арина — древние цивилизации\n⚔️ Катя — средневековье\n🚀 Артемий — новейшая история\n\n💻 Кирилл — разработчик бота");

        } else if (command.equalsIgnoreCase("📅 Сегодня в истории")) {
            messageService.sendMessage(chatId, "📅 *Сегодня в истории*\n\nКалендарь исторических событий пока в разработке. Анна и Арина составляют список важных дат!");

        } else if (command.equalsIgnoreCase("🔙 Назад") ||
                   command.equalsIgnoreCase("Назад")) {

            userStateService.setUserState(userId, UserState.MAIN_MENU);
            messageService.sendMessageWithKeyboard(chatId,
                    "🔙 Возвращаемся в главное меню:",
                    KeyboardFactory.createMainKeyboard());

        } else if (command.equalsIgnoreCase("⭐ Избранное")) {
            messageService.sendMessage(chatId, "⭐ *Избранное*\n\nЗдесь будут сохраняться понравившиеся факты. Пока функция в разработке!");

        } else if (command.equalsIgnoreCase("⭐ В избранное")) {
            messageService.sendMessage(chatId, "⭐ *Добавлено в избранное!*\n\nФункция сохранения фактов скоро будет доступна.");

        } else {
            messageService.sendMessage(chatId, "Используйте кнопки ниже или напишите /start");
        }
    }

    private void handleRandomFact(long chatId, Long userId) {
        Fact fact = factService.getRandomFact();

        if (fact != null) {
            factService.incrementViews(fact.getId());
            userStateService.setUserState(userId, UserState.VIEWING_FACT);

            String message = formatFactMessage(fact);
            messageService.sendMessageWithKeyboard(chatId, message,
                    KeyboardFactory.createFactActionsKeyboard());
        } else {
            messageService.sendMessage(chatId, "📭 Пока нет фактов в базе. Скоро добавлю!");
        }
    }

    private void showCategories(long chatId, Long userId) {
        List<Category> categories = categoryService.getAllCategories();

        if (!categories.isEmpty()) {
            userStateService.setUserState(userId, UserState.CATEGORIES);

            StringBuilder message = new StringBuilder("🏷️ *Выберите категорию:*\n\n");
            for (Category category : categories) {
                message.append(category.getEmoji())
                        .append(" *")
                        .append(category.getName())
                        .append("* - ")
                        .append(category.getDescription())
                        .append("\n\n");
            }

            messageService.sendMessageWithKeyboard(chatId, message.toString(),
                    KeyboardFactory.createCategoriesKeyboard(categories));
        } else {
            messageService.sendMessage(chatId, "Категории пока не добавлены.");
        }
    }



    // Добавим в CommandHandler.java новые методы:

    private void handleFactCommand(long chatId) {
        FactService factService = new FactService();
        Fact fact = factService.getRandomFact();

        if (fact != null) {
            factService.incrementViews(fact.getId());

            String message = formatFactMessage(fact);
            messageService.sendMessage(chatId, message);
        } else {
            messageService.sendMessage(chatId, "📭 Пока нет фактов в базе. Скоро добавлю!");
        }
    }

    private void handleCategoriesCommand(long chatId) {
        CategoryService categoryService = new CategoryService();
        List<Category> categories = categoryService.getAllCategories();

        if (!categories.isEmpty()) {
            StringBuilder message = new StringBuilder("🏷️ *Категории фактов:*\n\n");

            for (Category category : categories) {
                message.append(category.getEmoji())
                        .append(" *")
                        .append(category.getName())
                        .append("*\n")
                        .append(category.getDescription())
                        .append("\n\n");
            }

            message.append("Выберите категорию для получения факта:");
            messageService.sendMessage(chatId, message.toString());
        } else {
            messageService.sendMessage(chatId, "Категории пока не добавлены.");
        }
    }

    private String formatFactMessage(Fact fact) {
        StringBuilder sb = new StringBuilder();
        sb.append("📜 *Исторический факт*\n\n");
        sb.append(fact.getContent()).append("\n\n");

        if (fact.getYear() != 0) {
            sb.append("🗓️ *Год:* ").append(fact.getYear()).append("\n");
        }

        if (fact.getPeriod() != null && !fact.getPeriod().isEmpty()) {
            sb.append("⏳ *Период:* ").append(fact.getPeriod()).append("\n");
        }

        sb.append("🏷️ *Категория:* ").append(fact.getCategory()).append("\n");

        if (fact.getSource() != null && !fact.getSource().isEmpty()) {
            sb.append("\n🔗 *Источник:* ").append(fact.getSource());
        }

        sb.append("\n\n👁️ *Просмотров:* ").append(fact.getViews());

        return sb.toString();
    }
}