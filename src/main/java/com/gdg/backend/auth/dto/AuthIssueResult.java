package com.gdg.backend.auth.dto;

import com.gdg.backend.user.domain.OauthProvider;
import com.gdg.backend.user.domain.Role;

public record AuthIssueResult(
        IssuedTokens tokens,
        Long userId,
        String email,
        String nickname,
        OauthProvider oauthProvider,
        Role role,
        boolean newUser
) {}
