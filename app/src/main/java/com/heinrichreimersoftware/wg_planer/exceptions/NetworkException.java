package com.heinrichreimersoftware.wg_planer.exceptions;

import java.io.IOException;

public class NetworkException extends IOException {
    public NetworkException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException() {
    }
}
