package com.avatar.trip.plan.exception;

public class TokenValidationException extends BusinessException{

    public TokenValidationException() {
    }

    public TokenValidationException(String message) {
        super(message);
    }
}
