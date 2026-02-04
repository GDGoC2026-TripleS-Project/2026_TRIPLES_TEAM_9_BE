package com.gdg.backend.api.user.dto;

import com.gdg.backend.api.user.domain.LearningField;
import com.gdg.backend.api.user.domain.LearningGoal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserProfileUpdateRequestDto {
    @NotBlank
    @Size(min = 2, max = 20)
    private String nickname;

    private LearningGoal learningGoal;

    private List<LearningField> learningFields;

    private String learningGoalText;

    private String resolution;
}
