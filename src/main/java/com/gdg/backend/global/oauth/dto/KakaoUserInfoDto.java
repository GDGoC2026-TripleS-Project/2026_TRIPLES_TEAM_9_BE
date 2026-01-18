package com.gdg.backend.global.oauth.dto;

import com.gdg.backend.global.oauth.dto.provider.KakaoUserResponseDto;
import com.gdg.backend.user.domain.OauthProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoUserInfoDto implements UserInfoDto{

    private final KakaoUserResponseDto attributes;

    @Override
    public String getProviderId(){
        return String.valueOf(attributes.getId().toString());
    }

    @Override
    public String getEmail() {
        if(attributes.getKakaoAccount() == null) return null;

        return attributes.getKakaoAccount().getEmail();
    }

    @Override
    public String getName(){
        if (attributes.getKakaoAccount() == null) return null;
        if (attributes.getKakaoAccount().getProfile() == null) return null;

        return attributes.getKakaoAccount().getProfile().getNickname();
    }

    @Override
    public String getProfileImageUrl(){
        if(attributes.getKakaoAccount() == null) return null;
        if (attributes.getKakaoAccount().getProfile() == null) return null;

        return attributes.getKakaoAccount().getProfile().getProfileImageUrl();
    }

    @Override
    public OauthProvider getProvider(){
        return OauthProvider.KAKAO;
    }

    @Builder
    public KakaoUserInfoDto(KakaoUserResponseDto attributes){
        this.attributes = attributes;
    }
}
