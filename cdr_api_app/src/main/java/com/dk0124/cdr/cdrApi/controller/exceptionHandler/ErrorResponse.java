package com.dk0124.cdr.cdrApi.controller.exceptionHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Link;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
    private final String  apiDocs = Link.of("/docs/index.html").getHref();
    //private Map params;
}
