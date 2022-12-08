package com.dk0124.cdr.cdrApi.controller;

import com.dk0124.cdr.cdrApi.exception.InvalidApiParameterException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CdrApiValidatorTest {

    CdrApiValidator apiValidator = new CdrApiValidator();

    @DisplayName("UNIT TEST :CdrApiValidator.checkPathParams(dataType,vendorCode,coinCode) 올바른 입력에 대한 테스트")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2}   ")
    @MethodSource("provide_valid_api_path_parameters")
    void validate_path_param_success(String dataType,
                                     String vendorCode,
                                     String coinCode
    ) {
        apiValidator.checkPathParams(dataType, vendorCode, coinCode);
    }

    private static Stream<Arguments> provide_valid_api_path_parameters() { // argument source method
        return ParameterSupplier.getValidPathParams();
    }


    @DisplayName("UNIT TEST :CdrApiValidator.checkPathParams(dataType,vendorCode,coinCode) 나쁜 입력에 대해 InvalidApiParameterException ")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2} ")
    @MethodSource("provide_invalid_api_path_parameters")
    void validate_path_param_fail(String dataType,
                                  String vendorCode,
                                  String coinCode
    ) {
        assertThrows(InvalidApiParameterException.class, () -> {
            apiValidator.checkPathParams(dataType, vendorCode, coinCode);
        });

    }

    private static Stream<Arguments> provide_invalid_api_path_parameters() { // argument source method
        return ParameterSupplier.getInvalidPathParams();
    }

    @DisplayName("UNIT TEST :CdrApiValidator.checkRequestParams(timestamp, size, page) 좋은 입력에 대해 테스트 ")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2} ")
    @MethodSource("provide_valid_api_request_parameters")
    void validate_request_param_success(Long timestamp, Integer size, Integer page
    ) {
        apiValidator.checkRequestParams(timestamp, size, page);
    }

    private static Stream<Arguments> provide_valid_api_request_parameters() { // argument source method
        return ParameterSupplier.getValidQueryParams();
    }


    @DisplayName("UNIT TEST :CdrApiValidator.checkRequestParams(timestamp, size, page) 나쁜 입력에 대해 InvalidApiParameterException ")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2} ")
    @MethodSource("provide_invalid_api_request_parameters")
    void validate_request_param_fail(Long timestamp, Integer size, Integer page
    ) {
        assertThrows(InvalidApiParameterException.class, () -> {
            apiValidator.checkRequestParams(timestamp, size, page);
        });


    }

    private static Stream<Arguments> provide_invalid_api_request_parameters() { // argument source method
        return ParameterSupplier.getInValidQueryParams();
    }


}