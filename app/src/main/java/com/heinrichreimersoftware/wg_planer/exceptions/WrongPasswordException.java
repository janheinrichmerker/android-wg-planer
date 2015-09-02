package com.heinrichreimersoftware.wg_planer.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
    }

    public WrongPasswordException(String detailMessage) {
        super(detailMessage);
    }

    public WrongPasswordException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WrongPasswordException(Throwable throwable) {
        super(throwable);
    }
}
