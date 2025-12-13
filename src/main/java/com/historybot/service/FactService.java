package com.historybot.service;

import com.historybot.database.DatabaseConfig;
import com.historybot.database.DatabaseUtils;
import com.historybot.model.Fact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactService {

    public Fact getRandomFact() {
        String sql = "SELECT * FROM facts WHERE verified = 1 ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return DatabaseUtils.mapResultSetToFact(rs);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения случайного факта: " + e.getMessage());
        }
        return null;
    }

    public Fact getRandomFactByCategory(String category) {
        // Убираем возможные пробелы в начале/конце
        String cleanCategory = category.trim();

        // Пробуем несколько вариантов поиска
        String[] searchPatterns = {
                cleanCategory,
                "%" + cleanCategory + "%",
                cleanCategory + "%",
                "%" + cleanCategory
        };

        for (String pattern : searchPatterns) {
            String sql = "SELECT * FROM facts WHERE category LIKE ? AND verified = 1 ORDER BY RANDOM() LIMIT 1";

            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, pattern);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println("DEBUG: Найден факт по паттерну: '" + pattern + "'");
                    return DatabaseUtils.mapResultSetToFact(rs);
                }
            } catch (SQLException e) {
                System.err.println("Ошибка поиска факта по категории '" + pattern + "': " + e.getMessage());
            }
        }

        System.out.println("DEBUG: Не найдено фактов для категории: '" + cleanCategory + "'");
        return null;
    }

    public List<Fact> getFactsByYear(int year) {
        List<Fact> facts = new ArrayList<>();
        String sql = "SELECT * FROM facts WHERE year = ? AND verified = 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                facts.add(DatabaseUtils.mapResultSetToFact(rs));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения фактов по году: " + e.getMessage());
        }
        return facts;
    }

    public void incrementViews(Long factId) {
        String sql = "UPDATE facts SET views = views + 1 WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, factId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка увеличения просмотров: " + e.getMessage());
        }
    }

    // Метод для получения факта по ID
    public Fact getFactById(Long factId) {
        String sql = "SELECT * FROM facts WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, factId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return DatabaseUtils.mapResultSetToFact(rs);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения факта по ID: " + e.getMessage());
        }
        return null;
    }
}