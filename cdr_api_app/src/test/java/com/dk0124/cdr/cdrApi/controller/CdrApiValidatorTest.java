package com.dk0124.cdr.cdrApi.controller;

import com.dk0124.cdr.cdrApi.exception.InvalidApiParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CdrApiValidatorTest {

    CdrApiValidator apiValidator = new CdrApiValidator();

    @DisplayName("TEST :CdrApiValidator.checkPathParams(dataType,vendorCode,coinCode) 올바른 입력에 대한 테스트")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2}   ")
    @MethodSource("provide_valid_api_path_parameters")
    void validate_path_param_success(String dataType,
                                     String vendorCode,
                                     String coinCode
    ) {
        //given
        //when & then
        apiValidator.checkPathParams(dataType, vendorCode, coinCode);
    }

    private static Stream<Arguments> provide_valid_api_path_parameters() { // argument source method
        return Stream.of(
                Arguments.of("ticks", "upbit", "KRW-BTC"),
                Arguments.of("orderbooks", "upbit", "KRW-BTC"),
                Arguments.of("candles", "upbit", "KRW-BTC"),
                Arguments.of("ticks", "bithumb", "BTC_KRW"),
                Arguments.of("orderbooks", "bithumb", "BTC_KRW"),
                Arguments.of("candles", "bithumb", "BTC_KRW")
        );
    }


    @DisplayName("TEST :CdrApiValidator.checkPathParams(dataType,vendorCode,coinCode) 나쁜 입력에 대해 InvalidApiParameterException ")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2} ")
    @MethodSource("provide_bad_api_path_parameters")
    void validate_path_param_fail(String dataType,
                                  String vendorCode,
                                  String coinCode
    ) {
        //given
        //when & then
        assertThrows(InvalidApiParameterException.class, () -> {
            apiValidator.checkPathParams(dataType, vendorCode, coinCode);
        });

    }

    private static Stream<Arguments> provide_bad_api_path_parameters() { // argument source method
        return Stream.of(
                Arguments.of("", "upbit", "KRW-BTC"),
                Arguments.of("orderbooks", "", "KRW-BTC"),
                Arguments.of("candles", "upbit", ""),
                Arguments.of("ticks", "bithumb", "xxx"),
                Arguments.of("orderbooks", "xxx", "BTC_KRW"),
                Arguments.of("xxx", "bithumb", "BTC_KRW")
        );
    }

    @DisplayName("TEST :CdrApiValidator.checkRequestParams(timestamp, size, page) 나쁜 입력에 대해 InvalidApiParameterException ")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2} ")
    @MethodSource("provide_bad_api_request_parameters")
    void validate_request_param_fail(Long timestamp, Integer size, Integer page
    ) {
        //given
        //when & then
        assertThrows(InvalidApiParameterException.class, () -> {
            apiValidator.checkRequestParams(timestamp, size, page);
        });


    }

    private static Stream<Arguments> provide_bad_api_request_parameters() { // argument source method
        return Stream.of(
                Arguments.of(1670252400000L - 10, 200, 0),
                Arguments.of(null, 200, 0),
                Arguments.of(System.currentTimeMillis() + 100, 2000 + 1, 100),
                Arguments.of(System.currentTimeMillis() + 100, 200, -1)
        );
    }

    @DisplayName("TEST :CdrApiValidator.checkRequestParams(timestamp, size, page) 좋은 입력에 대해 테스트 ")
    @ParameterizedTest(name = "index ={index}, dataType={0}, vendorCode={1}, coinCode={2} ")
    @MethodSource("provide_valid_api_request_parameters")
    void validate_request_param_success(Long timestamp, Integer size, Integer page
    ) {
        //given
        //when & then
        apiValidator.checkRequestParams(timestamp, size, page);
    }

    private static Stream<Arguments> provide_valid_api_request_parameters() { // argument source method
        return Stream.of(
                Arguments.of(1670252400000L + 10, 200, 0),
                Arguments.of(1670252400000L, 200, 0),
                Arguments.of(System.currentTimeMillis() + 100, 200, 100),
                Arguments.of(System.currentTimeMillis() + 100, 200, 200),
                Arguments.of(System.currentTimeMillis(), 200, 2000)

        );
    }
}