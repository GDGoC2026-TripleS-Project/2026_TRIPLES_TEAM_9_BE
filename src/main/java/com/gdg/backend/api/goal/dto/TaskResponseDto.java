package com.gdg.backend.api.goal.dto;

import com.gdg.backend.api.goal.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskResponseDto {

    private Long taskId;
    private String content;
    private boolean completed;

    public static TaskResponseDto from(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getContent(),
                task.isCompleted()
        );
    }
}
