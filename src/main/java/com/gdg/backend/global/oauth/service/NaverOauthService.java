package com.gdg.backend.global.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdg.backend.global.exception.BedRequestException;
import com.gdg.backend.global.oauth.dto.NaverUserInfoDto;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.global.oauth.dto.provider.NaverTokenDto;
import com.gdg.backend.global.oauth.dto.provider.NaverUserResponseDto;
import com.gdg.backend.user.domain.OauthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NaverOauthService extends SocialOauthService {

    public NaverOauthService(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Value("${naver.token-uri}")
    private String tokenUri;

    @Value("${naver.user-info-uri}")
    private String userInfoUri;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    @Override
    public OauthProvider getProviderType(){
        return OauthProvider.NAVER;
    }

    @Override
    public UserInfoDto getUserInfo(String code) {
        throw new BedRequestException("네이버는 사용할 수 없습니다.");
    }

    public UserInfoDto getUserInfo(String code, String state) throws Exception {
        try{
            String tokenJson = postForm(tokenUri, Map.of(
                    "grant_type", "authorization_code",
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "redirect_uri", redirectUri,
                    "code", code,
                    "state", state
            ));

            NaverTokenDto naverTokenDto = objectMapper.readValue(tokenJson, NaverTokenDto.class);

            String userJson = getUserInfoFromProvider(naverTokenDto.getAccessToken());

            NaverUserResponseDto userDto = objectMapper.readValue(userJson, NaverUserResponseDto.class);

            return NaverUserInfoDto.builder()
                    .attributes(userDto)
                    .build();
        } catch (Exception e) {
            throw new BedRequestException("네이버 토큰 파싱을 실패했습니다.");
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
