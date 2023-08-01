package ourtine.domain.enums;

public enum SessionStatus {
    INACTIVE ("세션 시작 전"),
    ACTIVE("세션 시작"),
/*    VOTING("투표 중"),*/
    END("세션 종료");
    private final String description;

    SessionStatus(String description) {
        this.description=description;
    }
}
