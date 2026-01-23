package com.gdg.backend.global.security;

import com.gdg.backend.user.domain.OauthProvider;

public record SignupPrincipal(
        OauthProvider oauthProvider,
        String providerId,
        String email
) {}
