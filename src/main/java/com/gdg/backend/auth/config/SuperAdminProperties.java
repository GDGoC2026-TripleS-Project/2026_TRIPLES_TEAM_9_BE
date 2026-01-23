package com.gdg.backend.auth.config;

import com.gdg.backend.user.domain.OauthProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "admin.super")
@Getter
@Setter
public class SuperAdminProperties {
    private String email;
    private OauthProvider oauthProvider;
    private String providerId;
}
