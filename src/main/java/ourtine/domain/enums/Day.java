package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonCreator
    public static Day from(String sub) {
        for (Day day : Day.values()) {
            if (day.getDescription().equals(sub)) {
                return day;
            }
        }
        return null;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
