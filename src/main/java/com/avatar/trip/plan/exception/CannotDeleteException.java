package com.avatar.trip.plan.exception;

public class CannotDeleteException extends BusinessException {

    public CannotDeleteException() {
    }

    public CannotDeleteException(String message) {
        super(message);
    }
}
