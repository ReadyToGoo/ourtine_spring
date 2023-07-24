package ourtine.domain.enums;

public enum MessageType {

    FRIENDREQUEST("친구신청"),
    FRIENDREJECT("친구거절"),
    FRIENDACCEPT("친구수락"),
    FRIENDDELETE("친구삭제"),
    HABITINVITE("습관초대"),
    HABITJOIN("초대에 의한 습관 참여"),
    ADMINMESSAGE("관리자 전달 메시지");

    private final String description;

    MessageType(String description) {
        this.description=description;
    }
}
