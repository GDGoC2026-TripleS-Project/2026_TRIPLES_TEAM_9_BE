package com.gdg.backend.api.achievements.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_badges",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_badges_user_badge",
                columnNames = {"user_id", "badge_id"}
        ),
        indexes = @Index(name = "idx_user_badges_user", columnList = "user_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge_id", nullable = false, length = 50)
    private BadgeId badgeId;

    @Column(name = "unlocked_at", nullable = false)
    private LocalDateTime unlockedAt;

    public static UserBadge of(Long userId, BadgeId badgeId, LocalDateTime unlockedAt) {
        return UserBadge.builder()
                .userId(userId)
                .badgeId(badgeId)
                .unlockedAt(unlockedAt)
                .build();
    }

}
