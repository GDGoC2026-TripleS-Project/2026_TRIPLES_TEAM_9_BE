package com.gdg.backend.api.auth.dto;

import com.gdg.backend.api.user.domain.LearningField;
import com.gdg.backend.api.user.domain.LearningGoal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class AuthOnboardingRequestDto {
    @NotBlank
    private String authToken;

    @NotBlank
    private String nickname;

    @NotNull
    private LearningGoal learningGoal;

    @Size(max = 100)
    private String learningGoalText;

    private List<LearningField> learningFields;

    @Size(max = 200)
    private String resolution;
}
