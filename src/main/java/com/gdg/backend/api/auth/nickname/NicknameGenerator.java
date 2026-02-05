package com.gdg.backend.api.auth.nickname;

import com.gdg.backend.api.global.security.SignupPrincipal;

public interface NicknameGenerator {
    String generate(SignupPrincipal principal);
}
