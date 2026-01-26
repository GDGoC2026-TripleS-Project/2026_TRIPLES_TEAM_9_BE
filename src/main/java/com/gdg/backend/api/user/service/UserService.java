package com.gdg.backend.api.user.service;

import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.dto.UserInfoResponseDto;
import com.gdg.backend.api.user.dto.UserUpdateRequestDto;
import com.gdg.backend.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(Long userId){
        User user = getUser(userId);

        return UserInfoResponseDto.from(user);
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        if (request.getNickname() != null) {
            user.updateNickname(request.getNickname());
        }

    }


    @Transactional
    public void deleteUser(Long userId){
        User user = getUser(userId);

        userRepository.delete(user);
    }

    private User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }


}
