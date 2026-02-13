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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String INVALID_REQUEST_MESSAGE = "요청 값이 올바르지 않습니다.";

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
        String message = sanitizeMessage(ex.getMessage());
        return ApiResponse.error(ErrorCode.INVALID_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ApiResponse.error(ErrorCode.INVALID_REQUEST, extractBindingMessage(ex.getBindingResult()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Object>> handleBindException(BindException ex) {
        return ApiResponse.error(ErrorCode.INVALID_REQUEST, extractBindingMessage(ex.getBindingResult()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(this::formatConstraintViolation)
                .orElse(INVALID_REQUEST_MESSAGE);
        return ApiResponse.error(ErrorCode.INVALID_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        String message = (name == null || name.isBlank())
                ? INVALID_REQUEST_MESSAGE
                : name + "(이)가 올바르지 않습니다.";
        return ApiResponse.error(ErrorCode.INVALID_REQUEST, message);
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

    private String extractBindingMessage(BindingResult bindingResult) {
        if (bindingResult == null) {
            return INVALID_REQUEST_MESSAGE;
        }
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError == null) {
            return INVALID_REQUEST_MESSAGE;
        }
        String defaultMessage = fieldError.getDefaultMessage();
        if (defaultMessage != null && !defaultMessage.isBlank()) {
            return defaultMessage;
        }
        String field = fieldError.getField();
        if (field == null || field.isBlank()) {
            return INVALID_REQUEST_MESSAGE;
        }
        return field + ": " + INVALID_REQUEST_MESSAGE;
    }

    private String formatConstraintViolation(ConstraintViolation<?> violation) {
        String message = violation.getMessage();
        if (message == null || message.isBlank()) {
            return INVALID_REQUEST_MESSAGE;
        }
        String path = Optional.ofNullable(violation.getPropertyPath())
                .map(Object::toString)
                .orElse("");
        String field = extractLastPathSegment(path);
        if (field.isBlank()) {
            return message;
        }
        return field + ": " + message;
    }

    private String extractLastPathSegment(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        int lastDot = path.lastIndexOf('.');
        return lastDot >= 0 ? path.substring(lastDot + 1) : path;
    }

    private String sanitizeMessage(String message) {
        if (message == null || message.isBlank()) {
            return INVALID_REQUEST_MESSAGE;
        }
        if (message.startsWith("No enum constant")) {
            return INVALID_REQUEST_MESSAGE;
        }
        return message;
    }
}
