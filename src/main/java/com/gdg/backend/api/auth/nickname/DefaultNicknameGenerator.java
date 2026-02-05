package com.gdg.backend.api.auth.nickname;

import com.gdg.backend.api.global.security.SignupPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultNicknameGenerator implements NicknameGenerator {

    private final NicknameProperties properties;

    @Override
    public String generate(SignupPrincipal principal) {
        String raw = String.format(
                "%s_%s_%s",
                properties.getPrefix(),
                principal.oauthProvider().name().toLowerCase(),
                principal.providerId()
        );

        int maxLength = properties.getMaxLength();
        if (maxLength > 0 && raw.length() > maxLength) {
            return raw.substring(0, maxLength);
        }
        return raw;
    }
}
