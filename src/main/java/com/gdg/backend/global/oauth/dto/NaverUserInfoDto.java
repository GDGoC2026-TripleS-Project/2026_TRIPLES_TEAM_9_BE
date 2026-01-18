package com.gdg.backend.global.oauth.dto;

import com.gdg.backend.global.oauth.dto.provider.NaverUserResponseDto;
import com.gdg.backend.user.domain.OauthProvider;
import lombok.Builder;

public class NaverUserInfoDto implements UserInfoDto{

    private final NaverUserResponseDto attributes;

    @Override
    public String getProviderId(){
        if(attributes.getResponse() == null) return null;

        return attributes.getResponse().getId();
    }

    @Override
    public String getEmail() {
        if(attributes.getResponse() == null) return null;

        return attributes.getResponse().getEmail();
    }

    @Override
    public String getName(){
        if(attributes.getResponse() == null) return null;

        return attributes.getResponse().getName();
    }

    @Override
    public String getProfileImageUrl(){
        if(attributes.getResponse() == null) return null;

        return attributes.getResponse().getProfileImage();
    }

    @Override
    public OauthProvider getProvider(){
        return OauthProvider.NAVER;
    }

    @Builder
    public NaverUserInfoDto(NaverUserResponseDto attributes){
        this.attributes = attributes;
    }
}
