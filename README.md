# History Facts Bot

Telegram bot с историческими фактами.

## ⚠️ ВАЖНОЕ ПРЕДУПРЕЖДЕНИЕ

**Этот репозиторий содержит тестовый токен бота в открытом виде!**
- Токен хранится в файле `src/main/resources/config.properties`
- **НЕ ИСПОЛЬЗУЙТЕ** этот токен для продакшн-бота
- Для реального проекта используйте GitHub Secrets и не коммитьте токены

## Быстрый старт

### Локальный запуск

1. Убедитесь, что установлен Java 21 и Maven
2. Склонируйте репозиторий
3. Запустите команду:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.historybot.Main"
   ```

### Запуск через GitHub Actions

Бот автоматически запускается на серверах GitHub при каждом пуше в ветку `main`.

1. Создайте репозиторий на GitHub
2. Загрузите код:
   ```bash
   git add .
   git commit -m "Initial commit"
   git push origin main
   ```
3. Перейдите в раздел **Actions** на GitHub
4. Workflow автоматически запустится и бот будет доступен 24/7

## Структура проекта

- `src/main/java/com/historybot/Main.java` — точка входа
- `src/main/java/com/historybot/bot/` — конфигурация бота
- `src/main/java/com/historybot/handler/` — обработка команд
- `src/main/java/com/historybot/service/` — бизнес-логика
- `src/main/java/com/historybot/model/` — модели данных
- `src/main/java/com/historybot/database/` — работа с БД
- `src/main/resources/config.properties` — конфигурация бота (токен и имя)

## Доступные команды

- `/start` — запуск бота
- `/categories` — список категорий фактов
- `/random` — случайный исторический факт
- `/favorites` — избранные факты
- `/today` — события в этот день
- `/team` — информация о команде