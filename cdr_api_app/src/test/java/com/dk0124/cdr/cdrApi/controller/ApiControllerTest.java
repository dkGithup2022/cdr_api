package com.dk0124.cdr.cdrApi.controller;

import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.constants.vendor.VendorType;

import com.dk0124.cdr.persistence.entity.upbit.tick.UpbitTick;
import com.dk0124.cdr.persistence.entity.upbit.tick.UpbitTickFactory;
import com.dk0124.cdr.persistence.repository.upbit.upbitTickRepository.UpbitTickRepository;
import com.dk0124.cdr.persistence.repositoryPicker.upbit.UpbitTickRepositoryPicker;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("container")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Testcontainers
class ApiControllerTest {
    @Container
    static PostgreSQLContainer container
            = new PostgreSQLContainer().withDatabaseName("testDb");

    private MockMvc mockMvc;

    @Autowired
    UpbitTickRepositoryPicker upbitTickRepositoryPicker;

    String BASE_URL = "/cdrapi";

    String UPBIT_TICKS_REQUEST_URI =
            "/cdrapi/ticks/" + VendorType.UPBIT.getName() + "/" + UpbitCoinCode.KRW_ADA.toString();

    String UPBIT_ORDERBOOKS_REQUEST_URI =
            "/cdrapi/orderbooks/" + VendorType.UPBIT + "/" + UpbitCoinCode.KRW_ADA;

    String UPBIT_CANDLES_REQUEST_URI =
            "/cdrapi/candles/" + VendorType.UPBIT + "/" + UpbitCoinCode.KRW_ADA;

    String BITHUMB_TICKS_REQUEST_URI =
            "/cdrapi/ticks/" + VendorType.BITHUMB + "/" + BithumbCoinCode.KRW_ADA;

    String BITHUMB_ORDERBOOKS_REQUEST_URI =
            "/cdrapi/orderbooks/" + VendorType.BITHUMB + "/" + BithumbCoinCode.KRW_ADA;

    String BITHUMB_CANDLES_REQUEST_URI =
            "/cdrapi/candles/" + VendorType.BITHUMB + "/" + BithumbCoinCode.KRW_ADA;


    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
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
        save1000Ticks();
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
                .andExpect(jsonPath("contents[0].sequentialId").exists())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].type").exists())
                .andExpect(jsonPath("contents[0].tradePrice").exists())
                .andExpect(jsonPath("contents[0].tradeVolume").exists())
                .andExpect(jsonPath("contents[0].askBid").exists())
                .andExpect(jsonPath("contents[0].prev_closing_price").exists())
                .andExpect(jsonPath("contents[0].chage").exists())
                .andExpect(jsonPath("contents[0].chage_price").exists())
                .andExpect(jsonPath("contents[0].tradeDateUtc").exists())
                .andExpect(jsonPath("contents[0].tradeTimeUtc").exists())
                .andExpect(jsonPath("contents[0].tradeTimestamp").exists())
                .andExpect(jsonPath("contents[0]timestamp").exists())
        ;
    }

    @Test
    @DisplayName("API TEST : upbit orderbooks 에 dto 칼럼들 존재하는지 확인 ")
    public void check_body_upbit_orderbookss_columns() throws Exception {
        String url = UPBIT_ORDERBOOKS_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].timestamp").exists())
                .andExpect(jsonPath("contents[0].totalAskSize").exists())
                .andExpect(jsonPath("contents[0].totalBidSize").exists())
                .andExpect(jsonPath("contents[0].orderbook_units[0].askPrice").exists())
                .andExpect(jsonPath("contents[0].orderbook_units[0].bidPrice").exists())
                .andExpect(jsonPath("contents[0].orderbook_units[0].askSize").exists())
                .andExpect(jsonPath("contents[0].orderbook_units[0].bidSize").exists());
    }

    @Test
    @DisplayName("API TEST : upbit candles 에 dto 칼럼들 존재하는지 확인")
    public void check_body_upbit_candles_columns() throws Exception {
        String url = UPBIT_CANDLES_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].market").exists())
                .andExpect(jsonPath("contents[0].timestamp").exists())
                .andExpect(jsonPath("contents[0].candleDateTimeUtc").exists())
                .andExpect(jsonPath("contents[0].candleDateTimeKst").exists())
                .andExpect(jsonPath("contents[0].openingPrice").exists())
                .andExpect(jsonPath("contents[0].highPrice").exists())
                .andExpect(jsonPath("contents[0].lowPrice").exists())
                .andExpect(jsonPath("contents[0].tradePrice").exists())
                .andExpect(jsonPath("contents[0].candleAccTradePrice").exists())
                .andExpect(jsonPath("contents[0].candleAccTradeVolume").exists())
        ;
    }

    @Test
    @DisplayName("API TEST : bithumb ticks 에 dto 칼럼들 존재하는지 확인")
    public void check_body_bithumb_ticks_columns() throws Exception {
        String url = BITHUMB_TICKS_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].buySellGb").exists())
                .andExpect(jsonPath("contents[0].contPrice").exists())
                .andExpect(jsonPath("contents[0].contQty").exists())
                .andExpect(jsonPath("contents[0].contAmt").exists())
                .andExpect(jsonPath("contents[0].contDtm").exists())
                .andExpect(jsonPath("contents[0].dpdn").exists())
                .andExpect(jsonPath("contents[0].timestamp").exists())
        ;
    }

    @Test
    @DisplayName("API TEST : bithumb orderbooks 에 dto 칼럼들 존재하는지 확인 ")
    public void check_body_bithumb_orderbooks_columns() throws Exception {
        String url = BITHUMB_ORDERBOOKS_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].datetime").exists())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].orderbookUnits.orderType").exists())
                .andExpect(jsonPath("contents[0].orderbookUnits.price").exists())
                .andExpect(jsonPath("contents[0].orderbookUnits.quantity").exists())
                .andExpect(jsonPath("contents[0].orderbookUnits.total").exists());
    }

    @Test
    @DisplayName("API TEST : bithumb candles 에 dto 칼럼들 존재하는지 확인 ")
    public void check_body_bithumb_candles_columns() throws Exception {
        String url = BITHUMB_CANDLES_REQUEST_URI;
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(jsonPath("contents[0].code").exists())
                .andExpect(jsonPath("contents[0].timestamp").exists())
                .andExpect(jsonPath("contents[0].openingPrice").exists())
                .andExpect(jsonPath("contents[0].closingPrice").exists())
                .andExpect(jsonPath("contents[0].highPrice").exists())
                .andExpect(jsonPath("contents[0].lowPrice").exists())
                .andExpect(jsonPath("contents[0].tradeAmount").exists());
    }

    @Test
    @DisplayName("API TEST : api 호출 시, timestamp = 1669561200000 미만에서 400 에러 반환")
    public void check_catching_invalid_timestamp_0_with_400_error() throws Exception {
        String url = BITHUMB_CANDLES_REQUEST_URI +"?timestamp=0&size=200";
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /*
    TODO
    : timestamp 현재시 이상의 요청에서
     */
    @DisplayName("")
    @ParameterizedTest(name="{displayName} 's {index} trial :  {arguments} ")
    @CsvSource({"'home',20, false, false", "'',20, true, false","'home',0, false, true"})
    public void parameter_validation_check_200(
            String dataType,
            String vendorCode,
            String coinCode,
            long timestamp,
            int size
    ) throws Exception {
        String url = BASE_URL
                + "/" + dataType
                + "/" + vendorCode
                + "/" + coinCode
                + "?timestamp=" + timestamp +"&size=" +size
                 ;

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("event.update() 테스트 ")
    @ParameterizedTest(name="{displayName} 's {index} trial :  {arguments}  ")
    @CsvSource({"'home',20, false, false", "'',20, true, false","'home',0, false, true"})
    public void parameter_validation_check_400(
            String dataType,
            String vendorCode,
            String coinCode,
            long timestamp,
            int size
    ) throws Exception {
        String url = BASE_URL
                + "/" + dataType
                + "/" + vendorCode
                + "/" + coinCode
                + "?timestamp=" + timestamp +"&size=" +size
                ;

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private void save1000Ticks() {
        for (Long i = 1670252400000L; i < 1670252400000L + 1000; i++) {
            UpbitTick upbitTick = UpbitTick.builder()
                    .sequentialId(Long.valueOf(i))
                    .code(UpbitCoinCode.KRW_ADA.toString())
                    .timestamp(Long.valueOf(i))
                    .build();

            UpbitTickRepository repo =
                    upbitTickRepositoryPicker.getRepositoryFromCode(UpbitCoinCode.KRW_ADA);

            repo.save(UpbitTickFactory.of(upbitTick));
        }
    }


}