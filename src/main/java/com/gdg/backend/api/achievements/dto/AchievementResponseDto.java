package com.gdg.backend.api.achievements.dto;

import java.util.List;

public record AchievementResponseDto(
        Summary summary,
        List<AchievementSection> sections) {
}
