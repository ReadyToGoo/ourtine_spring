package ourtine.domain.enums;

public enum HabitFollowerStatus {

    COMPLETE("인증완료"),
    ENTERED("입장"),
    NOT_ENTERED("미입장");

    private String description;

    HabitFollowerStatus(String description){this.description = description;}
}
