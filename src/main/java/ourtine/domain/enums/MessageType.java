package ourtine.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MessageType {

    FOLLOW("팔로우"),
    HABIT_INVITE("습관초대"),
    HABIT_JOIN("초대에 의한 습관 참여"),
    ADMIN_MESSAGE("관리자 전달 메시지");

    private final String description;

    MessageType(String description) {
        this.description=description;
    }

    @JsonCreator
    public static MessageType convert(String source)
    {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.name().equals(source.toUpperCase())) {
                return messageType;
            }
        }
        return null;
    }

}
