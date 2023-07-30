package ourtine.domain.enums;

public enum CompleteStatus {

    COMPLETE("인증완료"),
    ENTERED("입장"),
    NOT_ENTERED("미입장");

    private String description;

    CompleteStatus(String description){this.description = description;}
}
