package ourtine.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

// 모든 ApiResponse 들의 MetaData
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private Boolean isSuccess;
    private String message;
    private T result;
}
