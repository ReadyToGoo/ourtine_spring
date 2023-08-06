package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryList {

    EXERCISE("운동"),
    READING("독서"),
    LANGUAGE("외국어"),
    MONEY("돈관리"),
    LIFESTYLE("생활습관"),
    STUDY("스터디"),
    HOBBY("취미생활"),
    CAREER("커리어");
    private final String description;

    @JsonCreator
    public static CategoryList from(String sub) {
        return CategoryList.valueOf(sub);
    }
}
