package com.historybot.service;

import com.historybot.model.UserState;
import java.util.HashMap;
import java.util.Map;

public class UserStateService {
    private final Map<Long, UserState> userStates = new HashMap<>();
    private final Map<Long, String> userSelectedCategories = new HashMap<>();
    private final Map<Long, Long> lastShownFact = new HashMap<>();
    private final Map<Long, Boolean> userChoseCategory = new HashMap<>(); // ← ДОБАВЬ ЭТО ПОЛЕ

    public UserState getUserState(Long userId) {
        return userStates.getOrDefault(userId, UserState.MAIN_MENU);
    }

    public void setUserState(Long userId, UserState state) {
        userStates.put(userId, state);
    }

    public void clearState(Long userId) {
        userStates.remove(userId);
        userSelectedCategories.remove(userId);
        lastShownFact.remove(userId);
        userChoseCategory.remove(userId); // ← ДОБАВЬ
    }

    public void setLastShownFact(Long userId, Long factId) {
        lastShownFact.put(userId, factId);
    }

    public Long getLastShownFact(Long userId) {
        return lastShownFact.get(userId);
    }

    public void clearLastShownFact(Long userId) {
        lastShownFact.remove(userId);
    }

    public void setSelectedCategory(Long userId, String category) {
        userSelectedCategories.put(userId, category);
    }

    public String getSelectedCategory(Long userId) {
        return userSelectedCategories.get(userId);
    }

    public void clearSelectedCategory(Long userId) {
        userSelectedCategories.remove(userId);
    }

    public void setUserChoseCategory(Long userId, boolean chose) {
        userChoseCategory.put(userId, chose);
    }

    public boolean didUserChooseCategory(Long userId) {
        return userChoseCategory.getOrDefault(userId, false);
    }

    public void clearUserChoseCategory(Long userId) {
        userChoseCategory.remove(userId);
    }
}