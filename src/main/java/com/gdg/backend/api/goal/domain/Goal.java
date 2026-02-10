package com.gdg.backend.api.goal.domain;

import com.gdg.backend.api.global.common.BaseTimeEntity;
import com.gdg.backend.api.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "goals")
public class Goal extends BaseTimeEntity {
    //목표 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //사용자 ID(로그인한 사용자인지 확인용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //목표 생성 시간
    @Column(name = "goal_created_at")
    private LocalDateTime CreatedAt;

    //목표 제목
    @Column(name = "goal_title", nullable = false, length = 500)
    private String title;

    //과제들
    @OneToMany(
            mappedBy = "goal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("createdAt ASC")
    private List<Task> tasks = new ArrayList<>();

    //목표 생성
    public static Goal create(User user, String title) {
        return Goal.builder()
                .user(user)
                .title(title)
                .build();
    }

    //완료된 과제 체크
    public int getCompletedTaskCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }

    //전체 과제 개수
    public int getTotalTaskCount() {
        return tasks.size();
    }
}
