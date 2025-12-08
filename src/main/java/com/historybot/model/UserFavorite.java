package com.historybot.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserFavorite {
    private Long id;
    private Long userId;            // ID пользователя в Telegram
    private Long factId;            // ID факта
    private LocalDateTime addedAt;
}