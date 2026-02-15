package com.gdg.backend.api.achievements.dto;

import java.time.LocalDateTime;

public record BadgeItem(
        String badgeId,
        String title,
        String description,
        boolean unlocked,
        LocalDateTime unlockedAt,
        int progress,
        int target
) {
}
