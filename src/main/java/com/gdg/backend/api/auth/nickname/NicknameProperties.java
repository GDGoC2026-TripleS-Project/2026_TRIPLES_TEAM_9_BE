package com.gdg.backend.api.auth.nickname;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth.nickname")
@Getter
@Setter
public class NicknameProperties {
    private String prefix = "user";
    private int maxLength = 20;
}
