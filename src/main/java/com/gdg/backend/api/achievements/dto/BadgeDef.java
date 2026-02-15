package com.gdg.backend.api.achievements.dto;

import com.gdg.backend.api.achievements.domain.BadgeId;

public record BadgeDef(
        BadgeId id,
        AchievementSection section,
        String title,
        String description,
        int target,
        int orderNo
) {
}
