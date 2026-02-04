package com.gdg.backend.api.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "user_learning_fields",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_learning_fields_user_field",
                columnNames = {"user_id", "field"}
        ),
        indexes = @Index(
                name = "idx_user_learning_fields_user_id",
                columnList = "user_id"
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLearningField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_learning_fields_user")
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "field", nullable = false, length = 50)
    private LearningField field;

    @Builder
    private UserLearningField(User user, LearningField field) {
        this.user = java.util.Objects.requireNonNull(user, "유저가 Null입니다.");
        this.field = java.util.Objects.requireNonNull(field, "필드가 Null입니다.");
    }

    public static UserLearningField from(User user, LearningField field) {
        return UserLearningField.builder()
                .user(user)
                .field(field)
                .build();
    }
}

