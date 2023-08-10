package ourtine.domain.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    NORMAL_ACTIVE("활성화 일반 유저"),
    NORMAL_INACTIVE("비활성화 일반 유저"),
    BANNED_ACTIVE("활성화 서비스 차단 유저"),
    BLOCKED_INACTIVE("비활성화 서비스 차단 유저");

    private final String description;

    UserStatus(String description) {
        this.description=description;
    }
}
