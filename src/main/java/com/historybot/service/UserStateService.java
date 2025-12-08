package com.historybot.service;

import com.historybot.model.UserState;
import java.util.HashMap;
import java.util.Map;

public class UserStateService {
    private final Map<Long, UserState> userStates = new HashMap<>();
    private final Map<Long, String> userSelectedCategories = new HashMap<>();

    public UserState getUserState(Long userId) {
        return userStates.getOrDefault(userId, UserState.MAIN_MENU);
    }

    public void setUserState(Long userId, UserState state) {
        userStates.put(userId, state);
    }

    public void setSelectedCategory(Long userId, String category) {
        userSelectedCategories.put(userId, category);
    }

    public String getSelectedCategory(Long userId) {
        return userSelectedCategories.get(userId);
    }

    public void clearState(Long userId) {
        userStates.remove(userId);
        userSelectedCategories.remove(userId);
    }
}