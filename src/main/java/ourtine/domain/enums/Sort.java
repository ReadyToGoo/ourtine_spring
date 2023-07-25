package ourtine.domain.enums;

public enum Sort {
    START_DATE("습관 시작일 순"),
    CREATED_DATE("습관 개설 순"),
    RECRUITING("모집중");
    private String description;

    Sort(String description){this.description = description;}
}
