package ourtine.domain.enums;

import lombok.Getter;

@Getter
public enum AuthProvider {
    KAKAO("kakao"),
    APPLE("apple");
    private final String provider;
    AuthProvider(String provider) {
        this.provider = provider;
    }
}
