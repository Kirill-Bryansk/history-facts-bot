package com.historybot.database;

import com.historybot.model.Category;
import com.historybot.model.Fact;
import com.historybot.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    public static Fact mapResultSetToFact(ResultSet rs) throws SQLException {
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

    public static Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setEmoji(rs.getString("emoji"));
        return category;
    }

    public static User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
        user.setLastActivity(rs.getTimestamp("last_activity").toLocalDateTime());
        return user;
    }
}