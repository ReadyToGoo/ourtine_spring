package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.hibernate.type.EnumType;

public enum Emotion  {
    VERY_BAD,
    BAD,
    MODERATE,
    GOOD,
    VERY_GOOD;

    @JsonCreator
    public static Emotion from(String sub) {
        return Emotion.valueOf(sub.toUpperCase());
    }
}
