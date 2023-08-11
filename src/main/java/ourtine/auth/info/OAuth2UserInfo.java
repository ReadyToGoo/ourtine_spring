package ourtine.auth.info;


import ourtine.domain.enums.AuthProvider;


/**
 * Kakao, Apple UserInfo가 공통적으로 구현해야할 스펙
 * 추후 소셜 로그인 Provider가 추가될 시, 이 인터페이스를 구현
 */
public interface OAuth2UserInfo {
//    Map<String, Object> getAttributes();
    Long getProviderId();
    AuthProvider getProvider();
//    String getEmail();
//    String getName();
}
