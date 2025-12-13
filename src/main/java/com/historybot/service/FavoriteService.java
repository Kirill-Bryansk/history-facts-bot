package com.historybot.service;

import com.historybot.database.DatabaseConfig;
import com.historybot.database.DatabaseUtils;
import com.historybot.model.Fact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteService {

    // Добавить факт в избранное
    public boolean addToFavorites(Long userId, Long factId) {
        String sql = "INSERT OR IGNORE INTO favorites (user_id, fact_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, factId);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка добавления в избранное: " + e.getMessage());
            return false;
        }
    }

    // Удалить факт из избранного
    public boolean removeFromFavorites(Long userId, Long factId) {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND fact_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, factId);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Ошибка удаления из избранного: " + e.getMessage());
            return false;
        }
    }

    // Проверить, добавлен ли факт в избранное
    public boolean isFavorite(Long userId, Long factId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND fact_id = ? LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, factId);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println("Ошибка проверки избранного: " + e.getMessage());
            return false;
        }
    }

    // Получить все избранные факты пользователя
    public List<Fact> getUserFavorites(Long userId) {
        List<Fact> favorites = new ArrayList<>();
        String sql = """
            SELECT f.* FROM facts f
            JOIN favorites fav ON f.id = fav.fact_id
            WHERE fav.user_id = ?
            ORDER BY fav.added_at DESC
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Fact fact = DatabaseUtils.mapResultSetToFact(rs);
                favorites.add(fact);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка получения избранного: " + e.getMessage());
        }
        return favorites;
    }

    // Получить количество избранных фактов
    public int getFavoritesCount(Long userId) {
        String sql = "SELECT COUNT(*) as count FROM favorites WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка подсчета избранного: " + e.getMessage());
        }
        return 0;
    }
}