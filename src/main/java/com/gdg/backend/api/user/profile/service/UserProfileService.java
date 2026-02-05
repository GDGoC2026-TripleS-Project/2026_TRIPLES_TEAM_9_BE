package com.gdg.backend.api.user.profile.service;

import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.user.profile.domain.LearningField;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.profile.domain.UserLearningField;
import com.gdg.backend.api.user.profile.domain.UserProfile;
import com.gdg.backend.api.user.profile.dto.UserProfileResponseDto;
import com.gdg.backend.api.user.profile.dto.UserProfileUpdateRequestDto;
import com.gdg.backend.api.user.profile.repository.UserLearningFieldRepository;
import com.gdg.backend.api.user.profile.repository.UserProfileRepository;
import com.gdg.backend.api.user.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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
                .orElse(UserProfile.create(user));

        log.info("[profile-update] userIdParam={}, user.getId={}, profile.userId={}, profile.user={}",
                userId,
                user.getId(),
                profile.getUserId(),
                profile.getUser() == null ? null : profile.getUser().getId());

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
