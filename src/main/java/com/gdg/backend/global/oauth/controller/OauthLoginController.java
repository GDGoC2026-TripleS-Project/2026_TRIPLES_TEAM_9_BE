package com.gdg.backend.global.oauth.controller;

import com.gdg.backend.global.oauth.factory.SocialOauthServiceFactory;
import com.gdg.backend.global.oauth.service.SocialOauthService;
import com.gdg.backend.user.domain.OauthProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "OAuth 로그인 시작")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class OauthLoginController {

    private final SocialOauthServiceFactory serviceFactory;

    @GetMapping("/{provider}")
    public void redirectToProvider(
            @PathVariable String provider,
            HttpServletResponse response
    ) throws IOException {

        OauthProvider oauthProviderEnum = OauthProvider.valueOf(provider.toUpperCase());
        SocialOauthService oauth = serviceFactory.get(oauthProviderEnum);

        String authorizationUrl = oauth.getAuthorizationUrl();
        log.info("OAuth authorize url ({}): {}", oauthProviderEnum, authorizationUrl);

        response.setStatus(HttpStatus.FOUND.value());
        response.sendRedirect(authorizationUrl);
    }
}
