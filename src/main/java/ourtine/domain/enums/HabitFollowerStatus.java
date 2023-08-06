package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HabitFollowerStatus {

    COMPLETE("인증완료"),
    ENTERED("입장"),
    NOT_ENTERED("미입장");

    private String description;

    HabitFollowerStatus(String description){this.description = description;}

}
