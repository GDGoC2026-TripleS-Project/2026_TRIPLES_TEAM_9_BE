package com.gdg.backend.api.achievements.repository;

import com.gdg.backend.api.achievements.domain.BadgeId;
import com.gdg.backend.api.achievements.domain.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    boolean existsByUserIdAndBadgeId(Long userId, BadgeId badgeId);
    Optional<UserBadge> findByUserIdAndBadgeId(Long userId, BadgeId badgeId);
    List<UserBadge> findAllByUserId(Long userId);
}
