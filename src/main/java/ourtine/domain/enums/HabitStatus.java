package ourtine.domain.enums;

public enum HabitStatus {

    PUBLIC("공개"),

    PRIVATE("비공개");

    private final String description;

    HabitStatus(String description) {
        this.description=description;
    }


}
