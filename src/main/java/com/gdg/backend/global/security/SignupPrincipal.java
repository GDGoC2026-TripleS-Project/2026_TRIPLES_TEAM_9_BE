package com.gdg.backend.global.security;

import com.gdg.backend.user.domain.OauthProvider;

public record SignupPrincipal(
        OauthProvider provider,
        String providerId,
        String email
) {}
