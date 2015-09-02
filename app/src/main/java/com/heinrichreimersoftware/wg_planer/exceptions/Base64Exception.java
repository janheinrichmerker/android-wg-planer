package com.heinrichreimersoftware.wg_planer.exceptions;

public class Base64Exception extends Exception {
    public Base64Exception() {
    }

    public Base64Exception(String detailMessage) {
        super(detailMessage);
    }

    public Base64Exception(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public Base64Exception(Throwable throwable) {
        super(throwable);
    }
}
