package com.gdg.backend.api.achievements.dto;

import java.util.List;

public record Section(
        String category,
        String title,
        List<BadgeItem> badges
) {}
