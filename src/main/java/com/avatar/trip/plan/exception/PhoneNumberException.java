package com.avatar.trip.plan.exception;

public class PhoneNumberException extends BusinessException{

    public PhoneNumberException() {
        super("휴대폰 번호를 잘못입력하셨습니다.");
    }

    public PhoneNumberException(String message) {
        super(message);
    }
}
