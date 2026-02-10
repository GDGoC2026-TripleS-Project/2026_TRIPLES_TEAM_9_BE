package com.gdg.backend.api.goal.dto;

import com.gdg.backend.api.goal.domain.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GoalListResponseDto {

    private Long goalId;
    private String title;

    private int completedTaskCount;
    private int totalTaskCount;

    public static GoalListResponseDto from(Goal goal) {
        return new GoalListResponseDto(
                goal.getId(),
                goal.getTitle(),
                goal.getCompletedTaskCount(),
                goal.getTotalTaskCount()
        );
    }
}
