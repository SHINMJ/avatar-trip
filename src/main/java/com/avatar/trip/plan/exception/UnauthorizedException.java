package com.avatar.trip.plan.exception;

public class UnauthorizedException extends BusinessException{

    public UnauthorizedException() {
        super("권한이 없습니다.");
    }
}
