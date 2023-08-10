package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;

@ApiModel
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
        return Day.valueOf(sub.toUpperCase());
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
