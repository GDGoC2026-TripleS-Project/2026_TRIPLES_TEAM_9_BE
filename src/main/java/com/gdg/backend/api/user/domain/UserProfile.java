package com.gdg.backend.api.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "learning_goal")
    private LearningGoal learningGoal;

    @Column(name = "learning_goal_text", length = 100)
    private String learningGoalText;

    @Column(name = "resolution", length = 200)
    private String resolution;

    protected UserProfile(User user) {
        this.user = user;
        this.userId = user.getId();
    }

    public static UserProfile of(User user) {
        return new UserProfile(user);
    }

    public void update(LearningGoal goal, String goalText, String resolution) {
        this.learningGoal = goal;
        this.learningGoalText = goalText;
        this.resolution = resolution;
    }
}

