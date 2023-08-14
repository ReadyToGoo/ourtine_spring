package ourtine.web.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ourtine.exception.enums.ResponseMessage;

import static ourtine.exception.enums.ResponseMessage.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "isSuccess", "message", "result"})
public class BaseResponseDto<T> {
    private final int code;
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponseDto(T result) {
        this.code = HttpStatus.OK.value();
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponseDto(ResponseMessage responseMessage) {
        this.code = responseMessage.getCode();
        this.isSuccess = responseMessage.isSuccess();
        this.message = responseMessage.getMessage();
    }

    public BaseResponseDto(int code, Boolean isSuccess, String errorMessage){
        this.code = code;
        this.isSuccess = isSuccess;
        this.message = errorMessage;
    }

}