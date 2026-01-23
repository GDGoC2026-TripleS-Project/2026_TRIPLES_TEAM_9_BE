package com.gdg.backend.api.global.oauth.dto;

import com.gdg.backend.api.user.domain.OauthProvider;

public interface UserInfoDto {
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImageUrl();
    OauthProvider getProvider();

}
