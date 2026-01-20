package com.gdg.backend.global.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdg.backend.global.exception.BedRequestException;
import com.gdg.backend.global.oauth.config.NaverProperties;
import com.gdg.backend.global.oauth.dto.NaverUserInfoDto;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.global.oauth.dto.provider.NaverTokenDto;
import com.gdg.backend.global.oauth.dto.provider.NaverUserResponseDto;
import com.gdg.backend.global.oauth.state.OAuthStateStore;
import com.gdg.backend.user.domain.OauthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class NaverOauthService extends SocialOauthService {

    private final NaverProperties props;
    private final OAuthStateStore stateStore;

    public NaverOauthService(
            ObjectMapper objectMapper,
            NaverProperties props,
            OAuthStateStore stateStore
    ) {
        super(objectMapper);
        this.props = props;
        this.stateStore = stateStore;
    }

    @Override
    public OauthProvider getProviderType() {
        return OauthProvider.NAVER;
    }

    @Override
    public UserInfoDto getUserInfo(String code) {
        throw new BedRequestException("네이버는 state가 필요합니다. getUserInfo(code, state)를 사용하세요.");
    }

    @Override
    public UserInfoDto getUserInfo(String code, String state) {
        try {
            String tokenJson = postForm(props.getTokenUri(), Map.of(
                    "grant_type", "authorization_code",
                    "client_id", props.getClientId(),
                    "client_secret", props.getClientSecret(),
                    "redirect_uri", props.getRedirectUri(),
                    "code", code,
                    "state", state
            ));

            log.info("naver token response: {}", tokenJson);

            NaverTokenDto tokenDto = objectMapper.readValue(tokenJson, NaverTokenDto.class);

            String userJson = getUserInfoFromProvider(tokenDto.getAccessToken());
            log.info("naver userinfo response: {}", userJson);

            NaverUserResponseDto userDto = objectMapper.readValue(userJson, NaverUserResponseDto.class);

            return NaverUserInfoDto.builder()
                    .attributes(userDto)
                    .build();

        } catch (Exception e) {
            throw new BedRequestException("네이버 유저 정보 조회/파싱을 실패했습니다. error: " + e.getMessage());
        }
    }

    @Override
    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        stateStore.save(state);

        return UriComponentsBuilder
                .fromUriString(props.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", props.getClientId())
                .queryParam("redirect_uri", props.getRedirectUri())
                .queryParam("state", state)
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
