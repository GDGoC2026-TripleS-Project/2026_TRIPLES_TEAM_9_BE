package com.gdg.backend.api.user.service;

import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.user.domain.LearningField;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.domain.UserLearningField;
import com.gdg.backend.api.user.domain.UserProfile;
import com.gdg.backend.api.user.dto.UserProfileResponseDto;
import com.gdg.backend.api.user.dto.UserProfileUpdateRequestDto;
import com.gdg.backend.api.user.repository.UserLearningFieldRepository;
import com.gdg.backend.api.user.repository.UserProfileRepository;
import com.gdg.backend.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserLearningFieldRepository userLearningFieldRepository;

    @Transactional(readOnly = true)
    public UserProfileResponseDto getMyProfile(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        UserProfile profile = userProfileRepository.findById(userId).orElse(null);
        return UserProfileResponseDto.from(
                user,
                profile,
                userLearningFieldRepository.findAllByUserId(userId)
        );
    }

    @Transactional
    public UserProfileResponseDto updateMyProfile(Long userId, UserProfileUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        UserProfile profile = userProfileRepository.findById(userId)
                .orElse(UserProfile.of(user));

        user.updateNickname(request.getNickname());
        profile.update(
                request.getLearningGoal(),
                request.getLearningGoalText(),
                request.getResolution()
        );
        userProfileRepository.save(profile);

        userLearningFieldRepository.deleteAllByUserId(userId);

        if (request.getLearningFields() != null) {
            for (LearningField field : request.getLearningFields()) {
                userLearningFieldRepository.save(
                        UserLearningField.from(user, field)
                );
            }
        }

        return UserProfileResponseDto.from(
                user,
                profile,
                userLearningFieldRepository.findAllByUserId(userId)
        );
    }
}
