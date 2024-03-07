package com.solaramps.api.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SolarAPIExceptionHandler extends ResponseEntityExceptionHandler {

    /*@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                getErrorDetail(e, e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                        FieldError::getField, FieldError::getDefaultMessage))));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap(e.getMethod(), e.getMessage())));
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }*/

    /*@ExceptionHandler(DisabledException.class)
    protected ResponseEntity<CustomErrorResponse> handleUnauthorized(DisabledException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("cause", e.getCause() != null ? e.getCause().toString() : null)));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }*/

    /*@ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<CustomErrorResponse> handleUnauthorized(BadCredentialsException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("cause", e.getCause() != null ? e.getCause().toString() : null)));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }*/

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<CustomErrorResponse> handleExceptionInternal(ConstraintViolationException e,
                                                                          WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("cause", e.getCause() != null ? e.getCause().toString() : null)));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<CustomErrorResponse> handleExceptionInternal(DataIntegrityViolationException e,
                                                                          WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("cause", e.getCause() != null ? e.getCause().toString() : null)));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<CustomErrorResponse> handleExceptionInternal(BadRequestException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("cause", e.getCause() != null ? e.getCause().toString() : null)));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(NotFoundException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("id", e.getId()))
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleAlreadyExistsException(AlreadyExistsException e,
                                                                            WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("id", e.getId()))
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidValueException(InvalidValueException e,
                                                                           WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("id", e.getId()))
        );
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomErrorResponse> handleForbiddenException(ForbiddenException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.singletonMap("value", e.getId()))
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SolarApiException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidValueException(SolarApiException e, WebRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                e.getMessage(),
                getErrorDetail(e, Collections.emptyMap())
        );
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Map<String, Object> getErrorDetail(Exception e, Map<String, Object> errors) {
        Map<String, Object> errorDetail = new HashMap<>();
        errorDetail.put("exception", e.getClass().getSimpleName());
        errorDetail.put("errors", errors);
        return errorDetail;
    }
}
