package com.gdg.backend.user.dto;

import com.gdg.backend.user.domain.OauthProvider;
import com.gdg.backend.user.domain.Role;
import com.gdg.backend.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private Role role;
    private OauthProvider oauthProvider;

    @Builder
    public UserInfoResponseDto(Long userId,
                               String email,
                               String nickname,
                               Role role,
                               OauthProvider oauthProvider){
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.oauthProvider = oauthProvider;
    }

    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .oauthProvider(user.getOauthProvider())
                .build();
    }
}
