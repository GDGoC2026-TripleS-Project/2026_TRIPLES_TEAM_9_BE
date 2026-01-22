package com.gdg.backend.user.dto;

import com.gdg.backend.user.domain.OauthProvider;
import com.gdg.backend.user.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String email;
    private String nickname;
    private OauthProvider oauthProvider;
    private Role role;
    private boolean newUser;

    @Builder
    public TokenResponseDto(String accessToken,
                            String refreshToken,
                            Long userId,
                            String email,
                            String nickname,
                            OauthProvider oauthProvider,
                            Role role,
                            boolean newUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.oauthProvider = oauthProvider;
        this.role = role;
        this.newUser = newUser;
    }
}
