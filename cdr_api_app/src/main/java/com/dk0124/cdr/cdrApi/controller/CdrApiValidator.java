package com.dk0124.cdr.cdrApi.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class CdrApiValidator {

    public boolean validatePath(String dataType,
                                String vendorCode,
                                String coinCode,
                                Errors error
    ){

        return true;
    }
}
