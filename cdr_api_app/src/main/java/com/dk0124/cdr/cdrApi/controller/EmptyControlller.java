package com.dk0124.cdr.cdrApi.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value ="/api/empty")
@ConditionalOnExpression("${conditions.enableEmptyApi:true}")
public class EmptyControlller {
    @GetMapping
    public ResponseEntity CallEmptyApi(){
        return ResponseEntity
                .ok()
                .header(HttpHeaders.HOST,linkTo(EmptyControlller.class).toUri().toString())
                .build();
    }
}
