package com.gdg.backend.api.user.dto;

import com.gdg.backend.api.user.domain.LearningField;
import com.gdg.backend.api.user.domain.LearningGoal;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.domain.UserLearningField;
import com.gdg.backend.api.user.domain.UserProfile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserProfileResponseDto {
    private Long userId;
    private String nickname;
    private LearningGoal learningGoal;
    private List<LearningField> learningFields;
    private String learningGoalText;
    private String resolution;

    @Builder
    public UserProfileResponseDto(
            Long userId,
            String nickname,
            LearningGoal learningGoal,
            List<LearningField> learningFields,
            String learningGoalText,
            String resolution
    ) {
        this.userId = userId;
        this.nickname = nickname;
        this.learningGoal = learningGoal;
        this.learningFields = learningFields;
        this.learningGoalText = learningGoalText;
        this.resolution = resolution;
    }

    public static UserProfileResponseDto from(User user, UserProfile profile, List<UserLearningField> fields) {
        return UserProfileResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .learningGoal(profile != null ? profile.getLearningGoal() : null)
                .learningGoalText(profile != null ? profile.getLearningGoalText() : null)
                .resolution(profile != null ? profile.getResolution() : null)
                .learningFields(fields == null ? List.of() : fields.stream()
                        .map(UserLearningField::getField)
                        .toList())
                .build();
    }
}
