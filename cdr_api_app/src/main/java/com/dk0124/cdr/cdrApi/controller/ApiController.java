package com.dk0124.cdr.cdrApi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cdrapi", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class ApiController {

    private final CdrApiValidator apiValidator;

    @RequestMapping("/{dataType}/{vendorCode}/{coinCode}")
    public ResponseEntity query(
            @PathVariable String dataType,
            @PathVariable String vendorCode,
            @PathVariable String coinCode,
            @RequestParam(required = false, name = "timestamp") @Min(1670252400000L) @Max(Long.MAX_VALUE) Optional<Long> timestamp,
            @RequestParam(required = false, name = "size")  @Min(1) @Max(2000) Optional<Integer> size,
            @RequestParam(required = false, name = "page")  Optional<Integer> page,
            Errors error
    ) {

        if(error.hasErrors())
            return ResponseEntity.badRequest().body(error);

        apiValidator.validatePath(dataType, vendorCode, coinCode, error);

        if(error.hasErrors())
            return ResponseEntity.badRequest().body(error);

        //Rquest Validator
        return ResponseEntity.ok("ok");
    }

    private long getValidTimestamp(Optional<Long> timestamp){
            return System.currentTimeMillis();
    }
    private int getValidSize(Optional<String> size){
        return 20;
    }
}
