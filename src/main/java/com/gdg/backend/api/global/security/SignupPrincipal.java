package com.gdg.backend.api.global.security;

import com.gdg.backend.api.user.domain.OauthProvider;

public record SignupPrincipal(
        OauthProvider oauthProvider,
        String providerId,
        String email
) {}
