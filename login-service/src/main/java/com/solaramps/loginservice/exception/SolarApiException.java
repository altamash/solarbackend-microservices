package com.solaramps.loginservice.exception;

public class SolarApiException extends RuntimeException {

    public SolarApiException(String message) {
        super(message);
    }

    public SolarApiException(String message, Throwable e) {
        super(message, e);
    }
}
