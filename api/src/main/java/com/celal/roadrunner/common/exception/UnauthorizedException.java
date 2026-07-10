package com.celal.roadrunner.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public UnauthorizedException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }
}
