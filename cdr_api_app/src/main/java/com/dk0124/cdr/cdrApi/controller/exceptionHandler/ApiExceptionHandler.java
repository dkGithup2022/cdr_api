package com.dk0124.cdr.cdrApi.controller.exceptionHandler;

import com.dk0124.cdr.cdrApi.exception.InvalidApiParameterException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(InvalidApiParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementFoundException(InvalidApiParameterException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getRawStatusCode(),exception.getMessage()));
    }
}
