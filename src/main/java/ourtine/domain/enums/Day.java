package ourtine.domain.enums;

public enum Day {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일");

    private final String description;

    Day(String description) {
        this.description = description;
    }
}
