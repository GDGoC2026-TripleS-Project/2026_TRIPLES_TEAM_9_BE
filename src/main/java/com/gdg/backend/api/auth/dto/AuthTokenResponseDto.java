package com.gdg.backend.api.auth.dto;

import com.gdg.backend.api.user.domain.OauthProvider;
import com.gdg.backend.api.user.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthTokenResponseDto {
    private String accessToken;
    private Long userId;
    private String email;
    private String nickname;
    private OauthProvider oauthProvider;
    private Role role;
    private boolean newUser;

    @Builder
    public AuthTokenResponseDto(String accessToken,
                                Long userId,
                                String email,
                                String nickname,
                                OauthProvider oauthProvider,
                                Role role,
                                boolean newUser) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.oauthProvider = oauthProvider;
        this.role = role;
        this.newUser = newUser;
    }
}
