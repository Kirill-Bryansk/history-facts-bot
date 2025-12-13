package com.historybot.service;

import com.historybot.database.DatabaseConfig;
import com.historybot.database.DatabaseUtils;
import com.historybot.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserService {

    // Регистрация или обновление пользователя
    public User getOrCreateUser(Message message) {
        Long userId = message.getFrom().getId();

        // Проверяем, существует ли пользователь
        Optional<User> existingUser = getUserById(userId);

        if (existingUser.isPresent()) {
            // Обновляем время последней активности
            updateLastActivity(userId);
            return existingUser.get();
        } else {
            // Создаем нового пользователя
            return createNewUser(message);
        }
    }

    private Optional<User> getUserById(Long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Используем DatabaseUtils для маппинга
                return Optional.of(DatabaseUtils.mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    private User createNewUser(Message message) {
        String sql = "INSERT INTO users (id, username, first_name, last_name) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, message.getFrom().getId());
            pstmt.setString(2, message.getFrom().getUserName());
            pstmt.setString(3, message.getFrom().getFirstName());
            pstmt.setString(4, message.getFrom().getLastName());
            pstmt.executeUpdate();

            // Создаем объект пользователя
            User user = new User();
            user.setId(message.getFrom().getId());
            user.setUsername(message.getFrom().getUserName());
            user.setFirstName(message.getFrom().getFirstName());
            user.setLastName(message.getFrom().getLastName());
            user.setRegisteredAt(LocalDateTime.now());
            user.setLastActivity(LocalDateTime.now());

            return user;

        } catch (SQLException e) {
            System.err.println("Ошибка создания пользователя: " + e.getMessage());
            throw new RuntimeException("Не удалось создать пользователя", e);
        }
    }

    private void updateLastActivity(Long userId) {
        String sql = "UPDATE users SET last_activity = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Ошибка обновления активности: " + e.getMessage());
        }
    }
}