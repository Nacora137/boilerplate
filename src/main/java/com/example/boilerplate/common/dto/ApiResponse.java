package com.example.boilerplate.common.dto;

import com.example.boilerplate.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private Result result;
    private T data;
    private String message;
    private String errorCode;
    private List<FieldError> errors;

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
        response.errors = new ArrayList<>();
        return response;
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, BindingResult bindingResult) {
        ApiResponse<T> response = error(errorCode);
        response.errors = FieldError.of(bindingResult);
        return response;
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, List<FieldError> errors) {
        ApiResponse<T> response = error(errorCode);
        response.errors = errors;
        return response;
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.result = Result.ERROR;
        response.message = message;
        response.errorCode = errorCode;
        response.errors = new ArrayList<>();
        return response;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
