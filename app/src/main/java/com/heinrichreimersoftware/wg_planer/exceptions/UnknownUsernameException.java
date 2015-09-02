package com.heinrichreimersoftware.wg_planer.exceptions;

public class UnknownUsernameException extends Exception {
    public UnknownUsernameException() {
    }

    public UnknownUsernameException(String detailMessage) {
        super(detailMessage);
    }

    public UnknownUsernameException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnknownUsernameException(Throwable throwable) {
        super(throwable);
    }
}
