package ourtine.domain.enums;

import lombok.Getter;

@Getter
public enum Provider {
    KAKAO("카카오"),
    APPLE("애플");

    private final String description;

    Provider(String description) {
        this.description=description;
    }
}
