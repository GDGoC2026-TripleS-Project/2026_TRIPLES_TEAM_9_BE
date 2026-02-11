package com.gdg.backend.api.goal.repository;

import com.gdg.backend.api.goal.domain.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Optional<Goal> findByIdAndUserId(Long id, Long userId);
}
