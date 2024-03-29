package com.avatar.trip.plan.exception;

public class CannotDeleteException extends BusinessException {

    public CannotDeleteException() {
        super("삭제할 수 없습니다. 관리자에게 문의하세요.");
    }

    public CannotDeleteException(String message) {
        super(message);
    }
}
