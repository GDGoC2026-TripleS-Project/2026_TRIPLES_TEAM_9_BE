package com.gdg.backend.global.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdg.backend.global.exception.BedRequestException;
import com.gdg.backend.global.oauth.config.GoogleProperties;
import com.gdg.backend.global.oauth.dto.GoogleUserInfoDto;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.global.oauth.dto.provider.GoogleTokenDto;
import com.gdg.backend.global.oauth.dto.provider.GoogleUserResponseDto;
import com.gdg.backend.user.domain.OauthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GoogleOauthService extends SocialOauthService {

    private final GoogleProperties props;

    public GoogleOauthService(ObjectMapper objectMapper, GoogleProperties props) {
        super(objectMapper);
        this.props = props;
    }

    @Override
    public OauthProvider getProviderType() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public UserInfoDto getUserInfo(String code) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", props.getClientId());
            params.put("redirect_uri", props.getRedirectUri());
            params.put("code", code);

            if (StringUtils.hasText(props.getClientSecret())) {
                params.put("client_secret", props.getClientSecret());
            }

            String tokenJson = postForm(props.getTokenUri(), params);
            log.info("google token response: {}", tokenJson);

            GoogleTokenDto tokenDto = objectMapper.readValue(tokenJson, GoogleTokenDto.class);

            if (!StringUtils.hasText(tokenDto.getAccessToken())) {
                throw new IllegalStateException("Google access_token is missing. tokenJson=" + tokenJson);
            }

            String userJson = getUserInfoFromProvider(tokenDto.getAccessToken());
            log.info("google userinfo response: {}", userJson);

            GoogleUserResponseDto userDto = objectMapper.readValue(userJson, GoogleUserResponseDto.class);

            return GoogleUserInfoDto.builder()
                    .attributes(userDto)
                    .build();

        } catch (Exception e) {
            throw new BedRequestException("구글 유저 정보 조회/파싱을 실패했습니다. error: " + e.getMessage());
        }
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUriString(props.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", props.getClientId())
                .queryParam("redirect_uri", props.getRedirectUri())
                .queryParam("scope", String.join(" ", props.getScope()))
                .build()
                .toUriString();
    }

    private String getUserInfoFromProvider(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return restTemplate.exchange(
                props.getUserInfoUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        ).getBody();
    }
}
