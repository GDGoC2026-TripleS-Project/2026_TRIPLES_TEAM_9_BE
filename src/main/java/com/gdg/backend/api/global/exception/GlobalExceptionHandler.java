package com.gdg.backend.api.global.exception;

import com.gdg.backend.api.auth.exception.AuthException;
import com.gdg.backend.api.global.code.ErrorCode;
import com.gdg.backend.api.global.exception.custom.RecordNotFoundException;
import com.gdg.backend.api.global.exception.custom.UserAlreadyExistsException;
import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException e) {
        log.warn("UserNotFoundException", e);
        return ApiResponse.error(ErrorCode.USER_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthException(AuthException e) {
        return ApiResponse.error(
                e.getErrorCode().getStatus().value(),
                e.getErrorCode().getMessage(),
                e.getErrorCode().getCode()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception occurred", e);
        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        return ApiResponse.error(ErrorCode.USER_ALREADY_EXISTS);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ApiResponse.error(ErrorCode.INVALID_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        return ApiResponse.error(
                ErrorCode.ACCESS_DENIED,
                e.getMessage()
        );
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecordNotFound(RecordNotFoundException e) {
        log.warn("RecordNotFoundException", e);
        return ApiResponse.error(ErrorCode.RECORD_NOT_FOUND, e.getMessage());
    }

}
