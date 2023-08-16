package ourtine.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum ResponseMessage {
    /**
     * 성공
     */
    SUCCESS(OK, true,  "요청에 성공하였습니다."),

    /**
     * 실패
     */
    INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, false,"요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다."),

    // USER
    WRONG_USER(NOT_FOUND,false, "해당 유저를 찾을 수 없습니다."),

    // HABIT
    WRONG_HABIT(NOT_FOUND,false,"해당 습관을 찾을 수 없습니다."),
    WRONG_HABIT_CATEGORY(NOT_FOUND,false,"해당 카테고리 찾을 수 없습니다."),
    WRONG_HABIT_FILE(BAD_REQUEST,false,"파일을 찾을 수 없습니다."),
    WRONG_HABIT_DELETE(NOT_FOUND,false,"해당 습관을 삭제할 수 없습니다."),
    WRONG_HABIT_JOIN(NOT_FOUND,false,"이미 습관에 참여하고 있습니다."),
    WRONG_HABIT_TIME(NOT_FOUND,false,"참여하고 있는 습관과 시간이 겹칩니다."),
    WRONG_HABIT_QUIT(NOT_FOUND,false,"이미 참여하고 있지 않은 습관입니다."),
    WRONG_HABIT_INVITE(NOT_FOUND,false,"초대할 수 있는 인원 수를 초과했습니다."),
    WRONG_HABIT_SEARCH(BAD_REQUEST,false, "습관을 검색할 수 없습니다."),

    // HABIT-SESSION
    WRONG_HABIT_SESSION(NOT_FOUND,false,"해당 습관 세션을 찾을 수 없습니다."),
    WRONG_HABIT_SESSION_VOTE(NOT_FOUND,false,"투표할 수 없는 유저입니다."),
    WRONG_HABIT_SESSION_FOLLOWER(NOT_FOUND,false,"해당 유저의 정보를 찾을 수 없습니다."),

    //FOLLOW
    WRONG_FOLLOW(NOT_FOUND,false,"이미 팔로우하고 있는 유저입니다."),
    WRONG_UNFOLLOW(NOT_FOUND,false,"이미 팔로우하고 있지 않은 유저입니다.")

    ;
    private final int code;
    private final boolean isSuccess;
    private final String message;

    ResponseMessage(HttpStatus code, boolean isSuccess, String message) {
        this.code = code.value();
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
