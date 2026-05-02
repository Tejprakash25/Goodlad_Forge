package com.goodlad.forge.common.exception;

import org.springframework.http.HttpStatus;

public class ForgeException extends RuntimeException {

    private final HttpStatus status;

    public ForgeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static ForgeException notFound(String message) {
        return new ForgeException(message, HttpStatus.NOT_FOUND);
    }

    public static ForgeException badRequest(String message) {
        return new ForgeException(message, HttpStatus.BAD_REQUEST);
    }

    public static ForgeException conflict(String message) {
        return new ForgeException(message, HttpStatus.CONFLICT);
    }
}
