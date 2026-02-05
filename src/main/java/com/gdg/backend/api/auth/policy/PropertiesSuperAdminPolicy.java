package com.gdg.backend.api.auth.policy;

import com.gdg.backend.api.auth.config.SuperAdminProperties;
import com.gdg.backend.api.global.security.SignupPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertiesSuperAdminPolicy implements SuperAdminPolicy {

    private final SuperAdminProperties superAdminProperties;

    @Override
    public boolean isSuperAdmin(SignupPrincipal principal) {
        if (superAdminProperties == null) {
            return false;
        }
        if (superAdminProperties.getOauthProvider() == null) {
            return false;
        }
        if (superAdminProperties.getProviderId() == null || superAdminProperties.getProviderId().isBlank()) {
            return false;
        }
        if (superAdminProperties.getEmail() == null || superAdminProperties.getEmail().isBlank()) {
            return false;
        }

        return principal.oauthProvider() == superAdminProperties.getOauthProvider()
                && principal.providerId().equals(superAdminProperties.getProviderId())
                && principal.email().equalsIgnoreCase(superAdminProperties.getEmail());
    }
}
