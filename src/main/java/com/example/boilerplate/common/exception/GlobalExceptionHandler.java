package com.example.boilerplate.common.exception;

import com.example.boilerplate.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * javax.validation.Valid or @Validated binding error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * @ModelAttribute binding error.
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ApiResponse<Void>> handleBindException(BindException e) {
        log.error("handleBindException", e);
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type mismatch error.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        String value = e.getValue() == null ? "" : e.getValue().toString();
        return new ResponseEntity<>(
                ApiResponse.error(ErrorCode.INVALID_TYPE_VALUE,
                        ApiResponse.FieldError.of(e.getName(), value, e.getErrorCode())),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Supported HTTP method mismatch error.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Body JSON parsing error.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.error("handleHttpMessageNotReadableException", e);
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE), HttpStatus.BAD_REQUEST);
    }

    /**
     * Access Denied error.
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.HANDLE_ACCESS_DENIED),
                ErrorCode.HANDLE_ACCESS_DENIED.getStatus());
    }

    /**
     * Custom Business Exception
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("handleBusinessException", e);
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(ApiResponse.error(errorCode), errorCode.getStatus());
    }

    /**
     * All other unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("handleException", e);
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
