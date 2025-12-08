package com.historybot.model;

import lombok.Data;

@Data
public class Category {
    private Long id;
    private String name;
    private String description;
    private String emoji;            // иконка для кнопок
}