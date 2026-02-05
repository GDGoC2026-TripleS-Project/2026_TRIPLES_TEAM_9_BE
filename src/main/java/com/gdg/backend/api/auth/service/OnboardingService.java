package com.gdg.backend.api.auth.service;

import com.gdg.backend.api.auth.dto.AuthOnboardingRequestDto;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.profile.domain.LearningField;
import com.gdg.backend.api.user.profile.domain.UserLearningField;
import com.gdg.backend.api.user.profile.domain.UserProfile;
import com.gdg.backend.api.user.profile.repository.UserLearningFieldRepository;
import com.gdg.backend.api.user.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserProfileRepository userProfileRepository;
    private final UserLearningFieldRepository userLearningFieldRepository;

    @Transactional
    public void apply(User user, AuthOnboardingRequestDto request) {
        user.updateNickname(request.getNickname());

        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElse(UserProfile.create(user));
        profile.update(
                request.getLearningGoal(),
                request.getLearningGoalText(),
                request.getResolution()
        );
        userProfileRepository.save(profile);

        syncLearningFields(user, request);
    }

    private void syncLearningFields(User user, AuthOnboardingRequestDto request) {
        userLearningFieldRepository.deleteAllByUserId(user.getId());
        if (request.getLearningFields() == null) {
            return;
        }
        for (LearningField field : request.getLearningFields()) {
            userLearningFieldRepository.save(UserLearningField.from(user, field));
        }
    }
}
