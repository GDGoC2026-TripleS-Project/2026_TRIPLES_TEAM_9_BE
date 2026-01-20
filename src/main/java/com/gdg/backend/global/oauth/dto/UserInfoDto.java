package com.gdg.backend.global.oauth.dto;

import com.gdg.backend.user.domain.OauthProvider;

public interface UserInfoDto {
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImageUrl();
    OauthProvider getProvider();

}
