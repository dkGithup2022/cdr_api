package com.dk0124.cdr.cdrApi.controller;

import com.dk0124.cdr.cdrApi.common.DataType;
import com.dk0124.cdr.cdrApi.service.bithumb.BithumbService;
import com.dk0124.cdr.cdrApi.service.upbit.UpbitService;
import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.constants.vendor.VendorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(value = "/cdrapi", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class ApiController {

    private final CdrApiValidator apiValidator;
    private final BithumbService bithumbService;
    private final UpbitService upbitService;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    @GetMapping("/{dataType}/{vendorCode}/{coinCode}")
    public ResponseEntity query(
            @PathVariable String dataType,
            @PathVariable String vendorCode,
            @PathVariable String coinCode,
            @RequestParam(required = false, name = "timestamp") Optional<Long> timestamp,
            @RequestParam(required = false, name = "size", defaultValue = "200") Integer size,
            @RequestParam(required = false, name = "page", defaultValue = "0") Integer page
    ) {
        // check validation
        Long convertedTimestamp = convertTimestampParam(timestamp);
        apiValidator.checkRequestParams(convertedTimestamp, size, page);
        apiValidator.checkPathParams(dataType, vendorCode, coinCode);

        //query data
        Page dataPage = queryData(dataType, vendorCode, coinCode, convertedTimestamp, size, page);

        // make response form
        PagedModel pagedModel = pagedResourcesAssembler.toModel(dataPage);

        //add link
        pagedModel.add(Link.of("docs/index.html").withRel("profile"));

        return ResponseEntity.ok(pagedModel);
    }


    private Page queryData(String dataType, String vendorCode, String coinCode, Long timestamp, Integer size, Integer page) {
        if (vendorCode.equals(VendorType.UPBIT.getName()))
            return queryUpbitData(dataType, coinCode, timestamp, size, page);

        if (vendorCode.equals(VendorType.BITHUMB.getName()))
            return queryBithumbData(dataType, coinCode, timestamp, size, page);

        return Page.empty();
    }

    private Page queryBithumbData(String dataType, String coinCode, Long timestamp, Integer size, Integer page) {
        BithumbCoinCode code = BithumbCoinCode.fromString(coinCode);

        if (dataType == DataType.TICKS.getName())
            return bithumbService.getBithumbticksBefore(code, timestamp, size, page);

        if (dataType == DataType.ORDERBOOKS.getName())
            return bithumbService.getBithumbOrderbooksBefore(code, timestamp, size, page);

        if (dataType == DataType.CANDLES.getName())
            return bithumbService.getBithumbCandlesBefore(code, timestamp, size, page);

        //TODO : throw new APIUNKNOWNEXCEPTION
        return Page.empty();
    }

    private Page queryUpbitData(String dataType, String coinCode, Long timestamp, Integer size, Integer page) {
        UpbitCoinCode code = UpbitCoinCode.fromString(coinCode);

        if (dataType.equals(DataType.TICKS.getName()))
            return upbitService.getUpbitTicksBefore(code, timestamp, size, page);

        if (dataType.equals(DataType.ORDERBOOKS.getName()))
            return upbitService.getUpbitOrderbooksBefore(code, timestamp, size, page);

        if (dataType.equals(DataType.CANDLES.getName()))
            return upbitService.getUpbitCandlesBefore(code, timestamp, size, page);

        //TODO : throw new APIUNKNOWNEXCEPTION
        return Page.empty();
    }


    private Long convertTimestampParam(Optional<Long> timestamp) {
        if (timestamp.isPresent())
            return timestamp.get();

        return System.currentTimeMillis();
    }
}
