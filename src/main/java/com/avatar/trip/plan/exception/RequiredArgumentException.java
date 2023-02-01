package com.avatar.trip.plan.exception;

public class RequiredArgumentException extends BusinessException{

    public RequiredArgumentException(String arg) {
        super(arg + "은(는) 필수입니다.");
    }
}
