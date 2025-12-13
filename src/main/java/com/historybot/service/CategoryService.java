package com.historybot.service;

import com.historybot.database.DatabaseConfig;
import com.historybot.database.DatabaseUtils;
import com.historybot.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(DatabaseUtils.mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения категорий: " + e.getMessage());
        }
        return categories;
    }
}