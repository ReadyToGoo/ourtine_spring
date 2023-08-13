package ourtine.auth.info.impl;


import ourtine.auth.info.OAuth2UserInfo;
import ourtine.domain.enums.Provider;


public class AppleOAuth2UserInfo implements OAuth2UserInfo {

    private Long appleId;

    public AppleOAuth2UserInfo(Long oauthId) {
        this.appleId = oauthId;
    }

    @Override
    public Long getProviderId() {
        return appleId;
    }

    @Override
    public Provider getProvider() {
        return Provider.APPLE;
    }
}
