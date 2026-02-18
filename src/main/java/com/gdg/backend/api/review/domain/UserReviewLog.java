package com.gdg.backend.api.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(
        name = "user_review_logs",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_review_logs_user_date_record",
                        columnNames = {"user_id", "viewed_date", "record_id"}
                )
        },
        indexes = {
                @Index(name = "idx_user_review_logs_user_date", columnList = "user_id, viewed_date")
        }
)
public class UserReviewLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "viewed_date", nullable = false)
    private LocalDate viewedDate;

    public static UserReviewLog of(Long userId, Long recordId, LocalDate viewedDate) {
        return UserReviewLog.builder()
                .userId(userId)
                .recordId(recordId)
                .viewedDate(viewedDate)
                .build();
    }
}
