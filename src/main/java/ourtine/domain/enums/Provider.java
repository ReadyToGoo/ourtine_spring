package ourtine.domain.enums;

import lombok.Getter;

@Getter
public enum Provider {
    KAKAO("kakao"),
    APPLE("apple");
    private final String provider;
    Provider(String provider) {
        this.provider = provider;
    }
}
