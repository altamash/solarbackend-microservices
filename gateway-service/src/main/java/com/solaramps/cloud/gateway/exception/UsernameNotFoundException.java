package com.solaramps.cloud.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UsernameNotFoundException extends SolarApiException {

    private Long id;

    public UsernameNotFoundException(String field, Long value) {
        super("Invalid " + field + ": " + value);
        this.id = value;
    }

    public UsernameNotFoundException(String message) {
        super(message);
    }
}
