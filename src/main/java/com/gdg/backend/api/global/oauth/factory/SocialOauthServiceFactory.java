package com.gdg.backend.api.global.oauth.factory;

import com.gdg.backend.api.global.oauth.service.SocialOauthService;
import com.gdg.backend.api.user.domain.OauthProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class SocialOauthServiceFactory {

    private final Map<OauthProvider, SocialOauthService> oauthServiceMap = new EnumMap<OauthProvider, SocialOauthService>(OauthProvider.class);

    public SocialOauthServiceFactory(List<SocialOauthService> services) {
        for (SocialOauthService service : services) {
            oauthServiceMap.put(service.getProviderType(), service);
        }
    }

    public SocialOauthService get(OauthProvider oauthProvider) {
        SocialOauthService service = oauthServiceMap.get(oauthProvider);
        if (service == null) {
            throw new IllegalStateException(
                    "No OAuth service registered for provider: " + oauthProvider
            );
        }
        return service;
    }


}
