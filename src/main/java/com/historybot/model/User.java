package com.historybot.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;                // Telegram user ID
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredAt;
    private LocalDateTime lastActivity;
}