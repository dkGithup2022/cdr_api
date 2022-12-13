package com.dk0124.cdr.cdrApi.controller;

import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.constants.vendor.VendorType;

import com.dk0124.cdr.persistence.entity.bithumb.candle.BithumbCandle;
import com.dk0124.cdr.persistence.entity.bithumb.candle.BithumbCandleFactory;
import com.dk0124.cdr.persistence.entity.bithumb.orderbook.BithumbOrderbook;
import com.dk0124.cdr.persistence.entity.bithumb.orderbook.BithumbOrderbookFactory;
import com.dk0124.cdr.persistence.entity.bithumb.orderbook.BithumbOrderbookUnit;
import com.dk0124.cdr.persistence.entity.bithumb.tick.BithumbTick;
import com.dk0124.cdr.persistence.entity.bithumb.tick.BithumbTickFactory;
import com.dk0124.cdr.persistence.entity.upbit.candle.UpbitCandle;
import com.dk0124.cdr.persistence.entity.upbit.candle.UpbitCandleFactory;
import com.dk0124.cdr.persistence.entity.upbit.orderbook.OrderBookUnit;
import com.dk0124.cdr.persistence.entity.upbit.orderbook.UpbitOrderBookFactory;
import com.dk0124.cdr.persistence.entity.upbit.orderbook.UpbitOrderbook;
import com.dk0124.cdr.persistence.entity.upbit.tick.UpbitTick;
import com.dk0124.cdr.persistence.entity.upbit.tick.UpbitTickFactory;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbCandleRepository.BithumbCandleCommonJpaInterface;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbOrderbookRepository.BithumbOrderbookCommonJpaInterface;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbTickRepository.BithumbTickCommonJpaInterface;
import com.dk0124.cdr.persistence.repository.upbit.upbitCandleRepository.UpbitCandleCommonJpaInterface;
import com.dk0124.cdr.persistence.repository.upbit.upbitOrderBookRepository.UpbitOrderbookCommonRepository;
import com.dk0124.cdr.persistence.repository.upbit.upbitTickRepository.UpbitTickRepository;
import com.dk0124.cdr.persistence.repositoryPicker.bithumb.BithumbCandleRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.bithumb.BithumbOrderbookRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.bithumb.BithumbTickRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.upbit.UpbitCandleRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.upbit.UpbitOrderbookRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.upbit.UpbitTickRepositoryPicker;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("container")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiControllerTest {
    @Container
    static PostgreSQLContainer container
            = new PostgreSQLContainer().withDatabaseName("testDb");

    private MockMvc mockMvc;

    @Autowired
    UpbitTickRepositoryPicker upbitTickRepositoryPicker;
    @Autowired
    UpbitOrderbookRepositoryPicker upbitOrderbookRepositoryPicker;
    @Autowired
    UpbitCandleRepositoryPicker upbitCandleRepositoryPicker;

    @Autowired
    BithumbTickRepositoryPicker bithumbTickRepositoryPicker;
    @Autowired
    BithumbOrderbookRepositoryPicker bithumbOrderbookRepositoryPicker;
    @Autowired
    BithumbCandleRepositoryPicker bithumbCandleRepositoryPicker;


    String BASE_URL = "/cdrapi";

    String UPBIT_TICKS_REQUEST_URI =
            "/cdrapi/ticks/" + VendorType.UPBIT.getName() + "/" + UpbitCoinCode.KRW_ADA.toString();

    String UPBIT_ORDERBOOKS_REQUEST_URI =
            "/cdrapi/orderbooks/" + VendorType.UPBIT.getName() + "/" + UpbitCoinCode.KRW_ADA;

    String UPBIT_CANDLES_REQUEST_URI =
            "/cdrapi/candles/" + VendorType.UPBIT.getName() + "/" + UpbitCoinCode.KRW_ADA;

    String BITHUMB_TICKS_REQUEST_URI =
            "/cdrapi/ticks/" + VendorType.BITHUMB.getName() + "/" + BithumbCoinCode.KRW_ADA;

    String BITHUMB_ORDERBOOKS_REQUEST_URI =
            "/cdrapi/orderbooks/" + VendorType.BITHUMB.getName() + "/" + BithumbCoinCode.KRW_ADA;

    String BITHUMB_CANDLES_REQUEST_URI =
            "/cdrapi/candles/" + VendorType.BITHUMB.getName() + "/" + BithumbCoinCode.KRW_ADA;


    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }


    @BeforeAll
    public void save_1000_data_each() {
        save_1000_upbit_ticks();
        save_1000_upbit_orderbook();
        save_1000_upbit_candle();

        save_1000_bithumb_tick();
        save_1000_bithumb_orderbook();
        save_1000_bithumb_candle();

    }

    @Test
    public void empty() {
        assertNotNull(container);
    }

    @Test
    @DisplayName("API TEST : _links 에 profile, self 링크 존재 여부 확인")
    //TODO : repeated test 형식으로 변경 가능
    public void check_header_is_valid() throws Exception {
        mockMvc.perform(get(UPBIT_TICKS_REQUEST_URI))
                .andDo(print())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.self").exists());

        mockMvc.perform(get(UPBIT_ORDERBOOKS_REQUEST_URI))
                .andDo(print())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.self").exists());

        mockMvc.perform(get(UPBIT_CANDLES_REQUEST_URI))
                .andDo(print())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.self").exists());

        mockMvc.perform(get(BITHUMB_TICKS_REQUEST_URI))
                .andDo(print())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.self").exists());

        mockMvc.perform(get(BITHUMB_ORDERBOOKS_REQUEST_URI))
                .andDo(print())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.self").exists());

        mockMvc.perform(get(BITHUMB_CANDLES_REQUEST_URI))
                .andDo(print())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.self").exists());
    }

    @Test
    @DisplayName("API TEST : _links 에 next,prev 링크 존재 여부 확인")
    //TODO : repeated test 형식으로 변경 가능
    public void check_header_is_valid_for_paging() throws Exception {
        String url = UPBIT_TICKS_REQUEST_URI + "?size=2&page=3";
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.prev").exists())
                .andExpect(jsonPath("_links.next").exists());
    }


    @Test
    @DisplayName("API TEST : upbit ticks 에 dto 칼럼들 존재하는지 확인")
    public void check_body_upbit_ticks_columns() throws Exception {
        String url = UPBIT_TICKS_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                // .andExpect(jsonPath("_embedded.upbitTickResourceList[0].sequentialId").exists())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].code").exists())
                // .andExpect(jsonPath("_embedded.upbitTickResourceList[0].type").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].tradePrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].tradeVolume").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].askBid").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].prevClosingPrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].change").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].changePrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].tradeDateUtc").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitTickResourceList[0].tradeTimeUtc").hasJsonPath())
                //   .andExpect(jsonPath("_embedded.upbitTickResourceList[0].timestamp").hasJsonPath())
                .andDo(document("upbit-ticks"
                        , responseHeaders(headerWithName(HttpHeaders.CONTENT_TYPE).description("response content type is application/hal"))
                        , requestParameters(
                                parameterWithName("timestamp").description("default : current timestamp, constraint: timestamp should be more than 1670252400000L"),
                                parameterWithName("size").description("maximum size of request, size is bigger than 0 and less than 2000"),
                                parameterWithName("page").description("page of request"))
                        , links(
                                linkWithRel("self").description("link self"),
                                linkWithRel("first").description("link of first page of this request"),
                                linkWithRel("prev").optional().description("link of previos page of this request, this link is ignored if there's no previous page of this request"),
                                linkWithRel("next").optional().description("link of next page of this request, this link is ignored if there's no next page of this request"),
                                linkWithRel("last").description("link of last page of this request"),
                                linkWithRel("profile").description("profile document link")
                        ), relaxedResponseFields(
                                fieldWithPath("_embedded.upbitTickResourceList[0].code").description("STRING: coin code of this data"),
                                fieldWithPath("_embedded.upbitTickResourceList[0].tradePrice").description("DOUBLE: current price of this coin"),
                                fieldWithPath("_embedded.upbitTickResourceList[0].tradeVolume").description("DOUBLE: recent trade volume "),
                                fieldWithPath("_embedded.upbitTickResourceList[0].prevClosingPrice").description("DOUBLE: last price of previos market "),
                                fieldWithPath("_embedded.upbitTickResourceList[0].change").description("String :  EVEN : 보합, RISE : 상승, FALL : 하락 "),
                                fieldWithPath("_embedded.upbitTickResourceList[0].changePrice").description("DOUBLE:  absolute value of change  "),
                                fieldWithPath("_embedded.upbitTickResourceList[0].tradeDateUtc").description("DATE: recent trade time : yyyyMMdd "),
                                fieldWithPath("_embedded.upbitTickResourceList[0].tradeTimeUtc").description("DATE: recent trade time : HHmmss "),
                                fieldWithPath("_embedded.upbitTickResourceList[0].timestamp").description("LONG: timestamp long vlaue  ")
                        )

                ));
        ;
    }

    @Test
    @DisplayName("API TEST : upbit orderbooks 에 dto 칼럼들 존재하는지 확인 ")
    public void check_body_upbit_orderbookss_columns() throws Exception {
        String url = UPBIT_ORDERBOOKS_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].code").exists())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].timestamp").exists())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].totalAskSize").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].totalBidSize").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].orderBookUnits[0].askPrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].orderBookUnits[0].bidPrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].orderBookUnits[0].askSize").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitOrderbookDtoList[0].orderBookUnits[0].bidSize").hasJsonPath());
    }

    @Test
    @DisplayName("API TEST : upbit candles 에 dto 칼럼들 존재하는지 확인")
    public void check_body_upbit_candles_columns() throws Exception {
        String url = UPBIT_CANDLES_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].market").exists())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].timestamp").exists())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].candleDateTimeUtc").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].candleDateTimeKst").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].openingPrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].highPrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].lowPrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].tradePrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].candleAccTradePrice").hasJsonPath())
                .andExpect(jsonPath("_embedded.upbitCandleDtoList[0].candleAccTradeVolume").hasJsonPath())
        ;
    }

    @Test
    @Disabled
    @DisplayName("API TEST : bithumb ticks 에 dto 칼럼들 존재하는지 확인")
    public void check_body_bithumb_ticks_columns() throws Exception {
        String url = BITHUMB_TICKS_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].timestamp").exists())
                .andExpect(jsonPath("contents[0].buySellGb").hasJsonPath())
                .andExpect(jsonPath("contents[0].contPrice").hasJsonPath())
                .andExpect(jsonPath("contents[0].contQty").hasJsonPath())
                .andExpect(jsonPath("contents[0].contAmt").hasJsonPath())
                .andExpect(jsonPath("contents[0].contDtm").hasJsonPath())
                .andExpect(jsonPath("contents[0].dpdn").hasJsonPath())
        ;
    }

    @Test
    @Disabled
    @DisplayName("API TEST : bithumb orderbooks 에 dto 칼럼들 존재하는지 확인 ")
    public void check_body_bithumb_orderbooks_columns() throws Exception {
        String url = BITHUMB_ORDERBOOKS_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].datetime").exists())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].orderbookUnits.orderType").hasJsonPath())
                .andExpect(jsonPath("contents[0].orderbookUnits.price").hasJsonPath())
                .andExpect(jsonPath("contents[0].orderbookUnits.quantity").hasJsonPath())
                .andExpect(jsonPath("contents[0].orderbookUnits.total").hasJsonPath());
    }

    @Test
    @Disabled
    @DisplayName("API TEST : bithumb candles 에 dto 칼럼들 존재하는지 확인 ")
    public void check_body_bithumb_candles_columns() throws Exception {
        String url = BITHUMB_CANDLES_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].timestamp").exists())
                .andExpect(jsonPath("contents[0].openingPrice").hasJsonPath())
                .andExpect(jsonPath("contents[0].closingPrice").hasJsonPath())
                .andExpect(jsonPath("contents[0].highPrice").hasJsonPath())
                .andExpect(jsonPath("contents[0].lowPrice").hasJsonPath())
                .andExpect(jsonPath("contents[0].tradeAmount").hasJsonPath());
    }

    @Test
    @DisplayName("API TEST : api 호출 시, timestamp = 1669561200000 미만에서 400 에러 반환")
    public void check_catching_invalid_timestamp_0_with_400_error() throws Exception {
        String url = BITHUMB_CANDLES_REQUEST_URI + "?timestamp=0&size=200";
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("apiDocs").exists())
        ;
    }

    /*
    TODO
    : timestamp 현재시 이상의 요청에서
     */
    @DisplayName("API TEST : 통합테스트, invalid request param 에 대해 InvalidApiParameterException ")
    @ParameterizedTest(name = "index ={index}, timestamp={0}, size={1}, page={2} ")
    @MethodSource("provide_valid_api_request_parameters")
    public void parameter_validation_check_200(
            Long timestamp,
            Integer size,
            Integer page
    ) throws Exception {
        String url = UPBIT_TICKS_REQUEST_URI
                + "?timestamp=" + timestamp + "&size=" + size + "&page=" + page;

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk());

    }

    private static Stream<Arguments> provide_valid_api_request_parameters() { // argument source method
        return ParameterSupplier.getValidQueryParams();
    }


    @DisplayName("API TEST : 통합테스트, invalid request param 에 대해 InvalidApiParameterException ")
    @ParameterizedTest(name = "index ={index}, timestamp={0}, size={1}, page={2} ")
    @MethodSource("provide_invalid_api_request_parameters")
    public void parameter_validation_check_400(
            Long timestamp,
            Integer size,
            Integer page
    ) throws Exception {

        String url = UPBIT_TICKS_REQUEST_URI
                + "?timestamp=" + timestamp + "&size=" + size + "&page=" + page;

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provide_invalid_api_request_parameters() { // argument source method
        return ParameterSupplier.getInValidQueryParams();
    }


    /*                   UTILS                */
    private void save_1000_upbit_ticks() {
        for (Long i = 1670252400000L; i < 1670252400000L + 1000; i++) {
            UpbitTick upbitTick = UpbitTick.builder()
                    .askBid("ask")
                    .change("change")
                    .type("sell")
                    .tradePrice(10.0)
                    .tradeVolume(100.0)
                    .prevClosingPrice(10.0)
                    .changePrice(10.0)
                    .tradeDateUtc(new Date())
                    .sequentialId(Long.valueOf(i))
                    .code(UpbitCoinCode.KRW_ADA.toString())
                    .timestamp(Long.valueOf(i))
                    .tradeTimeUtc(new Date())
                    .streamType("soc_stream")
                    .build();
            UpbitTickRepository repo =
                    upbitTickRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.KRW_ADA);
            repo.save(UpbitTickFactory.of(upbitTick));
        }
    }

    private void save_1000_upbit_orderbook() {
        for (Long i = 1670252400000L; i < 1670252400000L + 1000; i++) {
            UpbitOrderbook upbitOrderbook = UpbitOrderbook.builder()
                    .code(UpbitCoinCode.KRW_ADA.toString())
                    .timestamp(i)
                    .totalAskSize(10.0)
                    .totalBidSize(10.0)
                    .orderBookUnits(
                            Arrays.asList(
                                    OrderBookUnit.builder()
                                            .askPrice(10.0)
                                            .bidPrice(10.0)
                                            .askSize(10.0)
                                            .bidSize(10.0).build(),
                                    OrderBookUnit.builder()
                                            .askSize(20.0)
                                            .bidSize(20.0)
                                            .askPrice(20.0)
                                            .bidPrice(20.0).build()
                            )
                    )
                    .build();
            UpbitOrderbookCommonRepository repo =
                    upbitOrderbookRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.KRW_ADA);
            repo.save(UpbitOrderBookFactory.of(upbitOrderbook));
        }


    }

    private void save_1000_upbit_candle() {
        for (Long i = 1670252400000L; i < 1670252400000L + 1000; i++) {
            UpbitCandle upbitCandle = UpbitCandle
                    .builder()
                    .timestamp(i)
                    .market(UpbitCoinCode.KRW_ADA.toString())
                    .candleDateTimeUtc(new Date())
                    .candleDateTimeKst(new Date())
                    .candleAccTradePrice(10.0)
                    .candleAccTradeVolume(10.0)
                    .openingPrice(10.0)
                    .highPrice(10.0)
                    .lowPrice(10.0)
                    .tradePrice(10.0)
                    .build();

            UpbitCandleCommonJpaInterface repo = upbitCandleRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.KRW_ADA);
            repo.save(UpbitCandleFactory.of(upbitCandle));
        }
    }

    private void save_1000_bithumb_tick() {
        for (Long i = 1670252400000L; i < 1670252400000L + 1000; i++) {
            BithumbTick bithumbTick = BithumbTick
                    .builder()
                    .code(BithumbCoinCode.KRW_ADA.toString())
                    .buySellGb(10)
                    .contQty(10.0)
                    .contAmt(10.0)
                    .contDtm(new Date())
                    .dpdn("up")
                    .timestamp(i)
                    .build();

            BithumbTickCommonJpaInterface repo = bithumbTickRepositoryPicker.getRepositoryFromCode(BithumbCoinCode.KRW_ADA);
            repo.save(BithumbTickFactory.of(bithumbTick));
        }
    }

    private void save_1000_bithumb_orderbook() {
        for (Long i = 1670252400000L; i < 1670252400000L + 1000; i++) {
            BithumbOrderbook bithumbOrderbook = BithumbOrderbook
                    .builder()
                    .datetime(i)
                    .code(BithumbCoinCode.KRW_ADA.toString())
                    .orderbookUnits(
                            Arrays.asList(
                                    BithumbOrderbookUnit
                                            .builder()
                                            .orderType("sell")
                                            .price(10.0)
                                            .quantity(10.0)
                                            .total(100.0)
                                            .build(),
                                    BithumbOrderbookUnit
                                            .builder()
                                            .orderType("buy")
                                            .price(10.0)
                                            .quantity(10.0)
                                            .total(100.0)
                                            .build()
                            )
                    )
                    .build();
            BithumbOrderbookCommonJpaInterface repo = bithumbOrderbookRepositoryPicker.getRepositoryFromCode(BithumbCoinCode.KRW_ADA);
            repo.save(BithumbOrderbookFactory.of(bithumbOrderbook));
        }
    }

    private void save_1000_bithumb_candle() {
        for (Long i = 1670252400000L; i < 1670252400000L + 1000; i++) {

            BithumbCandle bithumbCandle = BithumbCandle.builder()
                    .code(BithumbCoinCode.KRW_ADA.toString())
                    .timestamp(i)
                    .openingPrice(10.0)
                    .closingPrice(10.0)
                    .highPrice(10.0)
                    .lowPrice(10.0)
                    .tradeAmount(10.0)
                    .build();
            BithumbCandleCommonJpaInterface repo = bithumbCandleRepositoryPicker.getRepositoryFromCode(BithumbCoinCode.KRW_ADA);
            repo.save(BithumbCandleFactory.of(bithumbCandle));
        }
    }


}