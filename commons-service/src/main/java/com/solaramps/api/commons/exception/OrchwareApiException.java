package com.solaramps.api.commons.exception;

public class OrchwareApiException extends RuntimeException {

    public OrchwareApiException(String message) {
        super(message);
    }

    public OrchwareApiException(String message, Throwable e) {
        super(message, e);
    }
}
