package ourtine.exception;

import lombok.Getter;
import ourtine.exception.enums.ResponseMessage;

@Getter
public class BusinessException extends RuntimeException{
    private final ResponseMessage responseMessage;

    public BusinessException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }
}