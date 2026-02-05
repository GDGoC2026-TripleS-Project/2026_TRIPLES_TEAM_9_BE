package com.gdg.backend.api.auth.policy;

import com.gdg.backend.api.global.security.SignupPrincipal;

public interface SuperAdminPolicy {
    boolean isSuperAdmin(SignupPrincipal principal);
}
