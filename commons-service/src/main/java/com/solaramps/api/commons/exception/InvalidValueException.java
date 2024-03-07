package com.solaramps.api.commons.exception;

import lombok.Getter;

@Getter
public class InvalidValueException extends OrchwareApiException {

    private Long id;

    public InvalidValueException(String field, String value) {
        super(value + " is not a valid " + field);
    }

    public InvalidValueException(String field) {
        super(field + " is required");
    }
}
