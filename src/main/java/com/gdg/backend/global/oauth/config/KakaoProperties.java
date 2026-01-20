package com.gdg.backend.global.oauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
