package com.gdg.backend.api.global.exception.custom;

public class BedRequestException extends RuntimeException {
    public BedRequestException(String message) {
        super(message);
    }
}
