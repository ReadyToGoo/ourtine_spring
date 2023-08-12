package ourtine.auth.info.impl;


import ourtine.auth.info.OAuth2UserInfo;
import ourtine.domain.enums.AuthProvider;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    // 카카오에서 제공하는 모든 attributes를 담는 코드확장성을 위한 코드 but, 빠른 구현을 위해 kakaoId 값만 집어넣기로.
    /*
    private Map<String, Object> attributes;

    private Map<String, Object> attributesAccount;
    private Map<String, Object> attributesProfile;


    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;

        @Override
        public Long getProviderId () {
            return Long.parseLong(attributes.get("id").toString());
        }

        @Override
        public AuthProvider getProvider () {
            return AuthProvider.KAKAO;
        }

        @Override
        public String getEmail () {
            return attributes.get("email").toString();
        }

        @Override
        public String getName () {
            return attributesProfile.get("nickname").toString();
        }


    }
    */
    private Long kakaoId;

    public KakaoOAuth2UserInfo(Long kakaoId){
        this.kakaoId = kakaoId;
    }

    @Override
    public Long getProviderId() {
        return kakaoId;
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.KAKAO;
    }
}
