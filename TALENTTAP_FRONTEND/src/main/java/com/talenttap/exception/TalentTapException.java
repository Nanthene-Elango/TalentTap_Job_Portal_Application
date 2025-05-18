package com.talenttap.exception;

public abstract class TalentTapException extends RuntimeException {
    private final int statusCode;

    public TalentTapException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

