package com.gdg.backend.global.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class IdTokenResponse {

    private String idToken;
    private String email;

    public static IdTokenResponse of(String idToken, String email){

        return IdTokenResponse.builder()
                .idToken(idToken)
                .email(email)
                .build();
    }

}
