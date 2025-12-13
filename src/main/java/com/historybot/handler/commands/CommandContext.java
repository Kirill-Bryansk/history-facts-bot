package com.historybot.handler.commands;

import com.historybot.service.*;

public class CommandContext {
    private final MessageService messageService;
    private final FactService factService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final UserStateService userStateService;

    public CommandContext(MessageService messageService,
                          FactService factService,
                          CategoryService categoryService,
                          UserService userService,
                          FavoriteService favoriteService,
                          UserStateService userStateService) {
        this.messageService = messageService;
        this.factService = factService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.favoriteService = favoriteService;
        this.userStateService = userStateService;
    }

    // Геттеры для всех сервисов
    public MessageService getMessageService() { return messageService; }
    public FactService getFactService() { return factService; }
    public CategoryService getCategoryService() { return categoryService; }
    public UserService getUserService() { return userService; }
    public FavoriteService getFavoriteService() { return favoriteService; }
    public UserStateService getUserStateService() { return userStateService; }
}