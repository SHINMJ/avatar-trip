package com.avatar.trip.plan.exception;

import com.avatar.trip.plan.exception.BusinessException;

public class WrongDateException extends BusinessException {

    public WrongDateException() {
        super("날짜를 잘못 입력하셨습니다.");
    }

    public WrongDateException(String message) {
        super(message);
    }
}
