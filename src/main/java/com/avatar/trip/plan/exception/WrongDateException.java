package com.avatar.trip.plan.exception;

import com.avatar.trip.plan.exception.BusinessException;

public class WrongDateException extends BusinessException {

    public WrongDateException() {
    }

    public WrongDateException(String message) {
        super(message);
    }
}
