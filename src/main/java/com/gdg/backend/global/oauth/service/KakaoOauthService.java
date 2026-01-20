package com.gdg.backend.global.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdg.backend.global.exception.BedRequestException;
import com.gdg.backend.global.oauth.config.KakaoProperties;
import com.gdg.backend.global.oauth.dto.KakaoUserInfoDto;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.global.oauth.dto.provider.KakaoTokenDto;
import com.gdg.backend.global.oauth.dto.provider.KakaoUserResponseDto;
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
public class KakaoOauthService extends SocialOauthService {

    private final KakaoProperties props;

    public KakaoOauthService(ObjectMapper objectMapper, KakaoProperties props) {
        super(objectMapper);
        this.props = props;
    }

    @Override
    public OauthProvider getProviderType() {
        return OauthProvider.KAKAO;
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
            log.info("kakao token response: {}", tokenJson);

            KakaoTokenDto tokenDto = objectMapper.readValue(tokenJson, KakaoTokenDto.class);

            if (!StringUtils.hasText(tokenDto.getAccessToken())) {
                throw new IllegalStateException("Kakao access_token is missing. tokenJson=" + tokenJson);
            }

            String userJson = getUserInfoFromProvider(tokenDto.getAccessToken());
            log.info("kakao userinfo response: {}", userJson);

            KakaoUserResponseDto userDto = objectMapper.readValue(userJson, KakaoUserResponseDto.class);

            return KakaoUserInfoDto.builder()
                    .attributes(userDto)
                    .build();

        } catch (Exception e) {
            throw new BedRequestException("카카오 유저 정보 조회/파싱을 실패했습니다. error: " + e.getMessage());
        }
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUriString(props.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", props.getClientId())
                .queryParam("redirect_uri", props.getRedirectUri())
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
