package com.gdg.backend.api.global.oauth.controller;

import com.gdg.backend.api.global.config.FrontendProperties;
import com.gdg.backend.api.global.jwt.TokenProvider;
import com.gdg.backend.api.global.oauth.dto.UserInfoDto;
import com.gdg.backend.api.global.oauth.factory.SocialOauthServiceFactory;
import com.gdg.backend.api.global.oauth.service.SocialOauthService;
import com.gdg.backend.api.global.oauth.state.OAuthStateStore;
import com.gdg.backend.api.user.domain.OauthProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "OAuth 로그인 콜백")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class OauthCallbackController {

    private final SocialOauthServiceFactory serviceFactory;
    private final TokenProvider tokenProvider;
    private final OAuthStateStore stateStore;
    private final FrontendProperties frontendProperties;

    @GetMapping("/callback/{provider}")
    public void callback(
            @PathVariable String provider,
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state,
            HttpServletResponse response
    ) {
        try {
            OauthProvider oauthProviderEnum = OauthProvider.valueOf(provider.toUpperCase());
            SocialOauthService oauth = serviceFactory.get(oauthProviderEnum);

            if (oauthProviderEnum == OauthProvider.NAVER) {
                if (state == null || !stateStore.consume(state)) {
                    redirectToFrontend(response, provider, "error", "INVALID_STATE");
                    return;
                }
            }

            UserInfoDto userInfo = (oauthProviderEnum == OauthProvider.NAVER)
                    ? oauth.getUserInfo(code, state)
                    : oauth.getUserInfo(code);

            String authToken = tokenProvider.authToken(
                    userInfo.getProvider(),
                    userInfo.getProviderId(),
                    userInfo.getEmail()
            );

            redirectToFrontend(response, provider, "token", authToken);

        } catch (Exception e) {
            log.error("OAuth callback failed", e);
            try {
                redirectToFrontend(response, provider, "error", "OAUTH_FAILED");
            } catch (Exception ignore) {}
        }
    }

    private void redirectToFrontend(HttpServletResponse response, String provider, String key, String value) throws Exception {
        String base = frontendProperties.getRedirectUrl();
        String encoded = URLEncoder.encode(value, StandardCharsets.UTF_8);
        String url = base + "/oauth/callback/" + provider + "?" + key + "=" + encoded;
        response.sendRedirect(url);
    }
}
