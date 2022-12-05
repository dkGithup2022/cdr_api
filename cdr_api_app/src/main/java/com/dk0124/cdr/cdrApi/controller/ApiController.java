package com.dk0124.cdr.cdrApi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cdrapi")
public class ApiController {

    @RequestMapping("/{dataType}/{vendorCode}/{coinCode}")
    public ResponseEntity query(
            @PathVariable String dataType,
            @PathVariable String vendorCode,
            @PathVariable String coinCode,
            @RequestParam(required = false, name = "timestamp") Optional<String> timestampBeforeValidated,
            @RequestParam(required = false, name = "size") Optional<String> sizeBeforeValidated
    ){

        return ResponseEntity.ok("ok");
    }
}
