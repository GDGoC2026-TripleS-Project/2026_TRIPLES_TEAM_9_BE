package com.gdg.backend.global.response;

import com.gdg.backend.global.code.ErrorCode;
import com.gdg.backend.global.code.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ApiResponse<T> {
    private final int code;
    private final boolean success;
    private final String message;
    private final HttpStatus status;
    private final T data;

    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode, T data) {
        return ResponseEntity.status(successCode.getStatus())
                .body(ApiResponse.<T>builder()
                        .code(successCode.getCode())
                        .success(true)
                        .message(successCode.getMessage())
                        .status(successCode.getStatus())
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data){
        return success(SuccessCode.OK, data);
    }

    public static ResponseEntity<ApiResponse<Object>> success(SuccessCode successCode){
        return success(successCode, null);
    }

    //error
    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.<T>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .success(false)
                        .status(errorCode.getStatus())
                        .data(null)
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(int status, String message) {
        return error(status, message, 0);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.<T>builder()
                        .code(errorCode.getCode())
                        .message(message)
                        .success(false)
                        .status(errorCode.getStatus())
                        .data(null)
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(int status, String message, int code) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .code(code)
                        .message(message)
                        .success(false)
                        .status(HttpStatus.valueOf(status))
                        .data(null)
                        .build());
    }

}
