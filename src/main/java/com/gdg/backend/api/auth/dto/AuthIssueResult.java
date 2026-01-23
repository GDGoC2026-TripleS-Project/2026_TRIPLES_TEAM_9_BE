package com.gdg.backend.api.auth.dto;

import com.gdg.backend.api.user.domain.OauthProvider;
import com.gdg.backend.api.user.domain.Role;

public record AuthIssueResult(
        IssuedTokens tokens,
        Long userId,
        String email,
        String nickname,
        OauthProvider oauthProvider,
        Role role,
        boolean newUser
) {}
