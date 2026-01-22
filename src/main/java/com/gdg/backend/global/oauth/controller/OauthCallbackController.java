package com.gdg.backend.global.oauth.controller;

import com.gdg.backend.global.jwt.TokenProvider;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.global.oauth.factory.SocialOauthServiceFactory;
import com.gdg.backend.global.oauth.service.SocialOauthService;
import com.gdg.backend.global.oauth.state.OAuthStateStore;
import com.gdg.backend.user.domain.OauthProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "OAuth 로그인 콜백")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class OauthCallbackController {

    private final SocialOauthServiceFactory serviceFactory;
    private final TokenProvider tokenProvider;
    private final OAuthStateStore stateStore;

    @GetMapping("/callback/{provider}")
    public ResponseEntity<Map<String, Object>> callback(
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
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("error", "INVALID_STATE"));
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

            return ResponseEntity.ok(Map.of(
                    "provider", oauthProviderEnum.name(),
                    "userInfo", userInfo,
                    "token", authToken
            ));

        } catch (Exception e) {
            log.error("OAuth callback failed", e);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "OAUTH_FAILED"));
        }
    }
}
