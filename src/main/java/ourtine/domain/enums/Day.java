package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Day {
    MON("MON"),
    TUE("TUE"),
    WED("WED"),
    THU("THU"),
    FRI("FRI"),
    SAT("SAT"),
    SUN("SUN");

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
