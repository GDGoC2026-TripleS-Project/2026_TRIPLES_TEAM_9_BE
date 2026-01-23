package com.gdg.backend.api.global.oauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "naver")
public class NaverProperties {
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
