package com.historybot.service;

import com.historybot.database.DatabaseConfig;
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
                return mapResultSetToFact(rs);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения случайного факта: " + e.getMessage());
        }
        return null;
    }

    public Fact getRandomFactByCategory(String category) {
        String sql = "SELECT * FROM facts WHERE category = ? AND verified = 1 ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFact(rs);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения факта по категории: " + e.getMessage());
        }
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
                facts.add(mapResultSetToFact(rs));
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

    private Fact mapResultSetToFact(ResultSet rs) throws SQLException {
        Fact fact = new Fact();
        fact.setId(rs.getLong("id"));
        fact.setContent(rs.getString("content"));
        fact.setCategory(rs.getString("category"));
        fact.setYear(rs.getInt("year"));
        fact.setPeriod(rs.getString("period"));
        fact.setSource(rs.getString("source"));
        fact.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        fact.setViews(rs.getInt("views"));
        fact.setVerified(rs.getBoolean("verified"));
        return fact;
    }
}