package com.historybot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:sqlite:history_bot.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection()) {
            // Создаем таблицу фактов
            String createFactsTable = """
                        CREATE TABLE IF NOT EXISTS facts (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            content TEXT NOT NULL,
                            category TEXT NOT NULL,
                            year INTEGER,
                            period TEXT,
                            source TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            views INTEGER DEFAULT 0,
                            verified BOOLEAN DEFAULT 1
                        )
                    """;

            // Создаем таблицу категорий
            String createCategoriesTable = """
                        CREATE TABLE IF NOT EXISTS categories (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT UNIQUE NOT NULL,
                            description TEXT,
                            emoji TEXT
                        )
                    """;

            // Создаем таблицу избранного
            String createFavoritesTable = """
                        CREATE TABLE IF NOT EXISTS favorites (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            user_id INTEGER NOT NULL,
                            fact_id INTEGER NOT NULL,
                            added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (fact_id) REFERENCES facts(id)
                        )
                    """;

            conn.createStatement().execute(createFactsTable);
            conn.createStatement().execute(createCategoriesTable);
            conn.createStatement().execute(createFavoritesTable);

            // Добавляем начальные данные
            initSampleData(conn);

        } catch (SQLException e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
        }
    }

    private static void initSampleData(Connection conn) throws SQLException {
        // Добавляем категории
        String insertCategories = """
                    INSERT OR IGNORE INTO categories (name, description, emoji) VALUES
                    ('Древний мир', 'Цивилизации Древнего Египта, Рима, Греции, Китая', '🏺'),
                    ('Средневековье', 'Эпоха рыцарей, замков и великих битв', '⚔️'),
                    ('Новое время', 'Эпоха Возрождения и Великих географических открытий', '🎨'),
                    ('Новейшая история', 'XX-XXI века', '🚀'),
                    ('Россия', 'История России', '🇷🇺'),
                    ('Наука', 'Исторические открытия и изобретения', '🔬'),
                    ('Культура', 'Искусство, музыка, литература', '🎭')
                """;

        // Добавляем тестовые факты
        String insertFacts = """
                    INSERT OR IGNORE INTO facts (content, category, year, period, source) VALUES
                    ('В Древнем Риме врачи использовали гладиаторскую кровь как лекарство от эпилепсии.', 'Древний мир', 100, 'Древний мир', 'https://ru.wikipedia.org'),
                    ('Средневековые рыцари носили под доспехами стеганые куртки, которые назывались "гамбезоны".', 'Средневековье', 1300, 'Средневековье', 'https://ru.wikipedia.org'),
                    ('Первый в мире программист была женщина - Ада Лавлейс, дочь лорда Байрона.', 'Наука', 1843, 'Новое время', 'https://ru.wikipedia.org'),
                    ('До 1917 года в России Новый год отмечали 1 сентября.', 'Россия', 1700, 'Новое время', 'https://ru.wikipedia.org')
                """;

        conn.createStatement().execute(insertCategories);
        conn.createStatement().execute(insertFacts);

        String insertMoreFacts = """
                    INSERT OR IGNORE INTO facts (content, category, year, period, source) VALUES
                    ('Первый компьютерный баг был реальным насекомым - мотыльком, застрявшим в реле компьютера Гарвардского университета в 1947 году.', 'Наука', 1947, 'Новейшая история', 'https://ru.wikipedia.org'),
                    ('В Древней Греции отсутствие зубов считалось признаком красоты у женщин.', 'Древний мир', -500, 'Древний мир', 'https://ru.wikipedia.org'),
                    ('Самый короткий период правления в истории - 20 минут. Им стал король Франции Луи XIX в 1830 году.', 'Новое время', 1830, 'Новое время', 'https://ru.wikipedia.org'),
                    ('В средневековой Европе считалось, что помидоры ядовиты, и их выращивали как декоративные растения.', 'Средневековье', 1500, 'Средневековье', 'https://ru.wikipedia.org'),
                    ('Первый в мире мультфильм был создан в России в 1912 году Владиславом Старевичем.', 'Культура', 1912, 'Новейшая история', 'https://ru.wikipedia.org'),
                    ('В Древнем Египте фараонов хоронили с фигурками слуг, которые должны были служить им в загробной жизни.', 'Древний мир', -2000, 'Древний мир', 'https://ru.wikipedia.org'),
                    ('Первая в мире фотография человека была сделана в 1838 году и экспозиция длилась 7 минут.', 'Наука', 1838, 'Новое время', 'https://ru.wikipedia.org')
                """;

        conn.createStatement().execute(insertMoreFacts);
    }
}
