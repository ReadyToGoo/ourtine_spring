package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sort {
    START_DATE("습관 시작일 순"),
    CREATED_DATE("습관 개설 순"),
    RECRUITING("모집중");
    private String description;

    Sort(String description){this.description = description;}

    @JsonCreator
    public static Sort from(String sub) {
        return Sort.valueOf(sub.toUpperCase());
    }
}
