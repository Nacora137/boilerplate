package com.example.boilerplate.common.dto;

import com.example.boilerplate.common.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private Result result;
    private T data;
    private String message;
    private String errorCode;

    public enum Result {
        SUCCESS, ERROR
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.result = Result.SUCCESS;
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.result = Result.ERROR;
        response.message = errorCode.getMessage();
        response.errorCode = errorCode.getCode();
        return response;
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.result = Result.ERROR;
        response.message = message;
        response.errorCode = errorCode;
        return response;
    }
}
