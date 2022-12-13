package com.dk0124.cdr.cdrApi.controller;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class ParameterSupplier {
    public static Stream<Arguments> getInValidQueryParams() { // argument source method
        return Stream.of(
                Arguments.of(1670252400000L - 10, 200, 0),
                Arguments.of(null, 200, 0),
                Arguments.of(System.currentTimeMillis() + 100, 2000 + 1, 100),
                Arguments.of(System.currentTimeMillis() + 100, 200, -1)
        );
    }

    public static Stream<Arguments> getValidQueryParams() { // argument source method
        return Stream.of(
                Arguments.of(1670252400000L + 10, 200, 0),
                Arguments.of(1670252400000L, 200, 0),
                Arguments.of(System.currentTimeMillis() + 100, 200, 100),
                Arguments.of(System.currentTimeMillis() + 100, 200, 200),
                Arguments.of(System.currentTimeMillis(), 200, 2000)
        );
    }

    public static Stream<Arguments> getInvalidPathParams() {
        return Stream.of(
                Arguments.of("", "upbit", "KRW-BTC"),
                Arguments.of("orderbooks", "", "KRW-BTC"),
                Arguments.of("candles", "upbit", ""),
                Arguments.of("ticks", "bithumb", "xxx"),
                Arguments.of("orderbooks", "xxx", "BTC_KRW"),
                Arguments.of("xxx", "bithumb", "BTC_KRW")
        );
    }

    public static Stream<Arguments> getValidPathParams() {
        return Stream.of(
                Arguments.of("ticks", "upbit", "KRW-BTC"),
                Arguments.of("orderbooks", "upbit", "KRW-BTC"),
                Arguments.of("candles", "upbit", "KRW-BTC"),
                Arguments.of("ticks", "bithumb", "BTC_KRW"),
                Arguments.of("orderbooks", "bithumb", "BTC_KRW"),
                Arguments.of("candles", "bithumb", "BTC_KRW")
        );
    }

}
