package com.dk0124.cdr.cdrApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/cdrapi", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class ApiController {

    private final CdrApiValidator apiValidator;

    @GetMapping("/{dataType}/{vendorCode}/{coinCode}")
    public ResponseEntity query(
            @PathVariable String dataType,
            @PathVariable String vendorCode,
            @PathVariable String coinCode,
            @RequestParam(required = false, name = "timestamp") Optional<Long> timestamp,
            @RequestParam(required = false, name = "size", defaultValue =  "200")  Integer size,
            @RequestParam(required = false, name = "page",defaultValue = "1") Optional<Integer> page
    ) {
        Long convertedTimestamp = convertTimestampParam(timestamp);
        apiValidator.checkRequestParams(convertedTimestamp, size);
        apiValidator.checkPathParams(dataType, vendorCode, coinCode);

        return ResponseEntity.ok("ok");
    }

    private Long convertTimestampParam(Optional<Long> timestamp){
        if (timestamp.isPresent())
            return timestamp.get();

        return System.currentTimeMillis();
    }
}
