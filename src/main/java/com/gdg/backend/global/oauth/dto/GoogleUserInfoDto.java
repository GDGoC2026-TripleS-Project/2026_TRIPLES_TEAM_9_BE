package com.gdg.backend.global.oauth.dto;

import com.gdg.backend.global.oauth.dto.provider.GoogleUserResponseDto;
import com.gdg.backend.user.domain.OauthProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GoogleUserInfoDto implements UserInfoDto{

    private final GoogleUserResponseDto attributes;

    @Override
    public String getProviderId() {
        if (attributes == null) return null;
        return attributes.getId();
    }

    @Override
    public String getEmail(){
        if(attributes == null) return null;
        return attributes.getEmail();
    }

    @Override
    public String getName(){
        if (attributes == null) return null;

        if (attributes.getName() != null) return attributes.getName();

        String given = attributes.getGivenName();
        String family = attributes.getFamilyName();

        if (given == null && family == null) return null;
        if (given == null) return family;
        if (family == null) return given;
        return family + given;
    }

    @Override
    public String getProfileImageUrl(){
        if (attributes == null) return null;
        return attributes.getPicture();
    }

    @Override
    public OauthProvider getProvider(){
        return OauthProvider.GOOGLE;
    }

    @Builder
    public GoogleUserInfoDto(GoogleUserResponseDto attributes) { this.attributes = attributes; }
}
