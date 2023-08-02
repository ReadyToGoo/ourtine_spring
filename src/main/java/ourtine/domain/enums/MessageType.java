package ourtine.domain.enums;

public enum MessageType {
    
    HABITINVITE("습관초대"),
    HABITJOIN("초대에 의한 습관 참여"),
    ADMINMESSAGE("관리자 전달 메시지");

    private final String description;

    MessageType(String description) {
        this.description=description;
    }
}
