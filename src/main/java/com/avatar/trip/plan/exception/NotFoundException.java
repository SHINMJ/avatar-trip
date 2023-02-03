package com.avatar.trip.plan.exception;

public class NotFoundException extends BusinessException{

    public NotFoundException() {
        super("해당 데이터를 찾을 수 없습니다.");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
