package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HabitStatus {

    PUBLIC("공개"),

    PRIVATE("비공개");

    private final String description;

    HabitStatus(String description) {
        this.description=description;
    }

    @JsonCreator
    public static HabitStatus convert(String source){
        for (HabitStatus day : HabitStatus.values()) {
            if (day.name().equals(source.toUpperCase())) {
                return day;
            }
        }
        return null;
    }

}
