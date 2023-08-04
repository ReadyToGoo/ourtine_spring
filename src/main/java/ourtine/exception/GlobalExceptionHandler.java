package ourtine.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ourtine.exception.enums.ResponseMessage;
import ourtine.web.dto.common.BaseResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponseDto<ResponseMessage> businessExceptionHandle(BusinessException e) {
        log.warn("businessException : {}", e);
        return new BaseResponseDto(e.getResponseMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponseDto<ResponseMessage> MethodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String firstErrorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();

        // 로그에 에러들 출력
        List<String> errorList = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
        System.out.println("errorList = " + errorList);
        String errorMsg = String.join(" | ", errorList);
        System.out.println("errorMsg = " + errorMsg);

        log.warn("MethodArgument:tNotValidExceptionException : {}", errorMsg);

        return new BaseResponseDto(HttpStatus.BAD_REQUEST.value(), false, firstErrorMessage);
    }
}
