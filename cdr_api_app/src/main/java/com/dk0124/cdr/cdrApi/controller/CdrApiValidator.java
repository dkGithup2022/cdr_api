package com.dk0124.cdr.cdrApi.controller;

import com.dk0124.cdr.cdrApi.common.DataType;
import com.dk0124.cdr.cdrApi.exception.InvalidApiParameterException;
import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.constants.vendor.VendorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class CdrApiValidator {
    public void checkPathParams(String dataType, String vendorCode, String coinCode) {
        if (isInValidDataType(dataType))
            throw new InvalidApiParameterException(HttpStatus.BAD_REQUEST,
                    "invalid data type code, only ticks, orderbooks, candles are allowed");

        if (isInValidVendorType(vendorCode))
            throw new InvalidApiParameterException(HttpStatus.BAD_REQUEST,
                    "invalid vendor code, only upbit, bithumb are allowed");

        if (isInValidCoinCode(vendorCode, coinCode)) {
            throw new InvalidApiParameterException(HttpStatus.BAD_REQUEST,
                    "invalid vendor code and coin code, check api profile link below ");
        }
    }

    public void checkRequestParams(Long timestamp, Integer size, Integer page) {
        if (isInvalidTime(timestamp))
            throw new InvalidApiParameterException(
                    HttpStatus.BAD_REQUEST,
                    "invalid timestamp , timestamp should be larger than 1670252400000L, and less than Long.MAX_VALUE");

        if (isInvalidSize(size))
            throw new InvalidApiParameterException(
                    HttpStatus.BAD_REQUEST,
                    "invalid size, size should be larger than 0, less than 2000");

        if (isInvalidPage(page))
            throw new InvalidApiParameterException(
                    HttpStatus.BAD_REQUEST,
                    "invalid page, page should be larger than -1");


    }

    private boolean isInvalidPage(Integer page) {
        return page == null || page < 0;
    }

    private boolean isInValidDataType(String dataType) {
        return dataType.isBlank() || DataType.fromString(dataType) == null;
    }

    private boolean isInValidVendorType(String vendorType) {
        return vendorType.isBlank() || VendorType.fromString(vendorType) == null;
    }

    private boolean isInValidCoinCode(String vendorCode, String coinCode) {
        if (vendorCode.equals(VendorType.BITHUMB.getName()))
            return BithumbCoinCode.fromString(coinCode) == null;

        if (vendorCode.equals(VendorType.UPBIT.getName()))
            return UpbitCoinCode.fromString(coinCode) == null;

        return true;
    }


    private boolean isInvalidTime(Long timestamp) {
        return timestamp == null || timestamp < 1670252400000L;
    }

    private boolean isInvalidSize(Integer size) {
        return size == null || size < 1 || size > 2000;
    }

}
