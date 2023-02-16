package com.avatar.trip.plan.exception;

public class TokenValidationException extends BusinessException{

    public TokenValidationException() {
        super("토큰이 잘못되었습니다.");
    }

    public TokenValidationException(String message) {
        super(message);
    }
}
