package com.gdg.backend.global.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdg.backend.global.exception.BedRequestException;
import com.gdg.backend.global.oauth.dto.KakaoUserInfoDto;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.global.oauth.dto.provider.KakaoTokenDto;
import com.gdg.backend.global.oauth.dto.provider.KakaoUserResponseDto;
import com.gdg.backend.user.domain.OauthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KakaoOauthService extends SocialOauthService {

    public KakaoOauthService(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public OauthProvider getProviderType(){
        return OauthProvider.KAKAO;
    }

    @Override
    public UserInfoDto getUserInfo(String code) throws Exception {
        try{
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", clientId);
            params.put("redirect_uri", redirectUri);
            params.put("code", code);

            if (StringUtils.hasText(clientSecret)) {
                params.put("client_secret", clientSecret);
            }
            String tokenJson = postForm(tokenUri, params);

            log.info(tokenJson);

            KakaoTokenDto kakaoTokenDto = objectMapper.readValue(tokenJson, KakaoTokenDto.class);

            String userJson = getUserInfoFromProvider(kakaoTokenDto.getAccessToken());

            KakaoUserResponseDto userDto = objectMapper.readValue(userJson, KakaoUserResponseDto.class);

            return KakaoUserInfoDto.builder()
                    .attributes(userDto)
                    .build();
        } catch (Exception e) {
            throw new BedRequestException("카카오 토큰 파싱을 실패했습니다.\n error: "+e.getMessage());
        }

    }

    private String getUserInfoFromProvider(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        return restTemplate.exchange(
                userInfoUri, HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        ).getBody();
    }
}
