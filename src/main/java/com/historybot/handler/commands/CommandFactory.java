package com.historybot.handler.commands;

import com.historybot.model.UserState;
import java.util.ArrayList;
import java.util.List;

public class CommandFactory {

    private final List<Command> commands;
    private final CommandContext context;

    public CommandFactory(CommandContext context) {
        this.context = context;
        this.commands = new ArrayList<>();
        registerCommands();
    }

    private void registerCommands() {
        // Специфичные команды для состояний (должны быть ПЕРВЫМИ)
        commands.add(new CategorySelectionCommand());    // для CATEGORIES
        commands.add(new MoreFromCategoryCommand());     // для VIEWING_FACT + "Ещё факт"
        commands.add(new FavoriteToggleCommand());       // для VIEWING_FACT + избранное

        // Общие команды (должны быть ПОСЛЕ специфичных)
        commands.add(new StartCommand());
        commands.add(new RandomFactCommand());          // только для MAIN_MENU
        commands.add(new CategoriesCommand());
        commands.add(new FavoritesCommand());
        commands.add(new TeamCommand());
        commands.add(new TodayInHistoryCommand());
        commands.add(new BackCommand());
    }

    public Command getCommand(String messageText, UserState userState) {
        System.out.println("=== DEBUG CommandFactory ===");
        System.out.println("Поиск команды для: '" + messageText + "'");
        System.out.println("Состояние пользователя: " + userState);

        for (Command command : commands) {
            boolean canHandle = command.canHandle(messageText, userState);
            System.out.println("  Команда " + command.getClass().getSimpleName() +
                               " canHandle: " + canHandle);
            if (canHandle) {
                return command;
            }
        }

        System.out.println("Команда не найдена");
        return null;
    }

    // Добавь этот метод:
    public CommandContext getContext() {
        return context;
    }
}