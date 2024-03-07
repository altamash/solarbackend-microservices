package com.solaramps.api.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class AlreadyExistsException extends SolarApiException {

    private Long id;

    public AlreadyExistsException(String userNameExists) {
        super(userNameExists + " is not available.");
        this.id = id;
    }

    public AlreadyExistsException(Class clazz, List<String> fields) {
        super(clazz.getSimpleName() + " with " + fields.toString().substring(1, fields.toString().length() - 1) + " " +
                "already exists");
    }

    public AlreadyExistsException(Class clazz, String field, String value) {
        super(clazz.getSimpleName() + " with '" + field + "' '" + value + "' already exists.");
    }
}
