package ourtine.auth.info;

import ourtine.auth.info.impl.AppleOAuth2UserInfo;
import ourtine.auth.info.impl.KakaoOAuth2UserInfo;
import ourtine.domain.enums.Provider;


public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Long oauthId) {
        if (registrationId.equalsIgnoreCase(Provider.KAKAO.toString())) {
            return new KakaoOAuth2UserInfo(oauthId);
        } else if (registrationId.equalsIgnoreCase(Provider.APPLE.toString())) {
            return new AppleOAuth2UserInfo(oauthId);
        }
        return null;
    }
}
