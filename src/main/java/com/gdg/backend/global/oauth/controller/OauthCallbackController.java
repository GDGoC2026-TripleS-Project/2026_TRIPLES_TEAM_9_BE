package com.gdg.backend.global.oauth.controller;

//import com.gdg.backend.global.config.FrontendProperties; //프론트 화면 구현시 사용
import com.gdg.backend.global.jwt.TokenProvider;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.global.oauth.factory.SocialOauthServiceFactory;
import com.gdg.backend.global.oauth.service.SocialOauthService;
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


@Tag(name = "Oauth를 이용하여 플랫폼 고유 아이디 가져오는 컨트롤러")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class OauthCallbackController {

    private final SocialOauthServiceFactory serviceFactory;
    private final TokenProvider tokenProvider;
    //private final FrontendProperties frontendProperties;

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

            UserInfoDto userInfo = (oauthProviderEnum == OauthProvider.NAVER)
                    ? oauth.getUserInfo(code, state)
                    : oauth.getUserInfo(code);

            String idToken = tokenProvider.idToken(
                    userInfo.getProvider(),
                    userInfo.getProviderId(),
                    userInfo.getEmail()
            );

            return ResponseEntity.ok(Map.of(
                    "userInfo", userInfo,
                    "token", idToken
            ));
//            String redirect =
//                    frontendProperties.getRedirectUrl()
//                            + "?idToken=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8);
//
//            response.sendRedirect(redirect);

        } catch (Exception e) {
            log.error("OAuth callback failed", e);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "OAUTH_FAILED"));

//            String errorRedirect =
//                    frontendProperties.getRedirectUrl()
//                            + "?error=OAUTH_FAILED";
//
//            response.sendRedirect(errorRedirect);

        }
    }
}

