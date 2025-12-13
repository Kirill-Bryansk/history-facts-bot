package com.historybot.util;

import com.historybot.model.Fact;

public class FactFormatter {

    public static String formatFactMessage(Fact fact) {
        if (fact == null || fact.getContent() == null || fact.getContent().isEmpty()) {
            return "📭 Факт не найден или поврежден.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📜 *Исторический факт*\n\n");
        sb.append(fact.getContent()).append("\n\n");

        if (fact.getYear() != 0) {
            sb.append("🗓️ *Год:* ").append(fact.getYear()).append("\n");
        }

        if (fact.getPeriod() != null && !fact.getPeriod().isEmpty()) {
            sb.append("⏳ *Период:* ").append(fact.getPeriod()).append("\n");
        }

        sb.append("🏷️ *Категория:* ").append(fact.getCategory()).append("\n");

        if (fact.getSource() != null && !fact.getSource().isEmpty()) {
            sb.append("\n🔗 *Источник:* ").append(fact.getSource());
        }

        sb.append("\n\n👁️ *Просмотров:* ").append(fact.getViews());

        return sb.toString();
    }
}