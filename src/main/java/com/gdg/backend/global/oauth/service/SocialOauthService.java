package com.gdg.backend.global.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdg.backend.global.oauth.dto.UserInfoDto;
import com.gdg.backend.user.domain.OauthProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@AllArgsConstructor
@Service
public abstract class SocialOauthService {
    protected final ObjectMapper objectMapper;
    protected final RestTemplate restTemplate = new RestTemplate();

    public abstract UserInfoDto getUserInfo(String code) throws Exception;

    public UserInfoDto getUserInfo(String code, String state) throws Exception {
        return  getUserInfo(code);
    };
    public abstract OauthProvider getProviderType();

    protected String postForm(String uri, Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
        );
        headers.setAccept(
                java.util.List.of(org.springframework.http.MediaType.APPLICATION_JSON)
        );

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        params.forEach(body::add);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(uri, request, String.class);

        return response.getBody();
    }

    private MultiValueMap<String, String> convert(Map<String, String> params) {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        params.forEach(result::add);
        return result;
    }
}
