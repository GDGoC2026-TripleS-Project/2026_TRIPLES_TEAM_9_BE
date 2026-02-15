package com.gdg.backend.api.achievements.dto;

public enum AchievementSection {
    LEARNING("LEARNING", "학습"),
    ATTENDANCE("ATTENDANCE", "출석"),
    CHALLENGE("CHALLENGE", "도전");

    private final String code;
    private final String title;

    AchievementSection(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String code() {
        return code;
    }

    public String title() {
        return title;
    }
}
