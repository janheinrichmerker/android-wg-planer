package com.heinrichreimersoftware.wg_planer.exceptions;

public class WrongCredentialsException extends Exception {
    public WrongCredentialsException() {
    }

    public WrongCredentialsException(String detailMessage) {
        super(detailMessage);
    }

    public WrongCredentialsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WrongCredentialsException(Throwable throwable) {
        super(throwable);
    }
}
