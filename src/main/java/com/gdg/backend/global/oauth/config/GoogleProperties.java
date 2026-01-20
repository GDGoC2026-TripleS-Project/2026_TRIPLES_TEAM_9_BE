package com.gdg.backend.global.oauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "google")
public class GoogleProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private List<String> scope;
}
