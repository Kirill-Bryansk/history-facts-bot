package com.historybot.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Fact {
    private Long id;
    private String content;          // текст факта
    private String category;         // категория
    private Integer year;            // год события
    private String period;           // период (Древний мир, Средневековье и т.д.)
    private String source;           // источник
    private LocalDateTime createdAt;
    private Integer views = 0;       // количество просмотров
    private Boolean verified = true; // проверен ли факт
}