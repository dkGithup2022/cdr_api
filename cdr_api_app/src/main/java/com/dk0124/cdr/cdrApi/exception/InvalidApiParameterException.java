package com.dk0124.cdr.cdrApi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Getter
@Setter
public class InvalidApiParameterException extends ResponseStatusException {
    public InvalidApiParameterException(HttpStatus status) {
        super(status);
    }
    public InvalidApiParameterException(HttpStatus status, String reason) {
        super(status, reason);
    }
    public InvalidApiParameterException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
