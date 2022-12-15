# CDR-API-PARENT

 <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white"/>

## 개요
 Upbit, bithumb의 ticks, candles, orderbook 정보를 제공합니다. 
자세한 정보와 스펙은 아래 링크를 참고해주세요

https://dankim0124.oopy.io/1473e902-1137-4755-9bab-3d613cd6afdf



## 모듈 구성
maven-multi-module 프로젝트입니다. 

    <modules>
        <module>cdr_common_constant</module>
        <module>cdr_common_persistence</module>
        <module>cdr_api_app</module>
    </modules>

cdr_common_constant : VendorCode, CoinCode 등 공용  Enum 의 클래스  
cdr_common_persistence : Persistence layer에 관련한 모듈입니다. entity, repository 등과 이를 지원하는 Utils 의 모듈  
cdr_api_app : rest controller 의 구현



## 기능


- endpoint : {url}/cdrapi/{dataType}/{vendorCode}/{coinCode}  
- 요청 예시 : /cdrapi/ticks/upbit/KRW-ADA  


### path parameters

1. dataType : ticks, orderbooks, candles
2. vendorCode : upbit, bithumb  
3. coinCode : 지원하는 정보의 리스트 
   
    
- bithumb : [KRW-BTC, KRW-XRP, KRW-ETH, KRW-STX, KRW-SOL, KRW-ADA, KRW-DOT, KRW-BCH, KRW-BAT, KRW-AVAX, KRW-ETC, KRW-AXS, KRW-PLA, KRW-SAND, KRW-SRM, KRW-DOGE, KRW-MANA, KRW-FLOW, KRW-BTG, KRW-ATOM, KRW-MATIC, KRW-ENJ, KRW-CHZ]


- upbit : [BTC_KRW, XRP_KRW, ETH_KRW, SOL_KRW, ADA_KRW, DOT_KRW, BCH_KRW, BAT_KRW, AVAX_KRW, ETC_KRW, AXS_KRW, PLA_KRW, SAND_KRW, SRM_KRW, DOGE_KRW, MANA_KRW, BTG_KRW, ATOM_KRW, MATIC_KRW, ENJ_KRW, CHZ_KRW]




### query parameters

limit 을 넘는 값에 대해선 400 응답이 반환됨.

* timestamp 
  * type : Long
  * default : 현재시 기준
  * limit : timestamp >= 1670252400000L 
  * etc : 현재시보다 큰 값이 입려될 경우, 현재시로 보정함.


* size
  * type : int
  * default : 200
  * limit : 1<={size}<=2000
  
  
  

# 응답 
HAL-JSON 형식의 응답을 반환하며 아래의 링크정보를 응답에 포함함.



* link
  * profile : api document 
  * next : req link for next page
  * prev : req link for prev page
  * last : last page link  for current req
  * first : first page link for current req


### Request  
```URI = /cdrapi/ticks/upbit/KRW-ADA```
  
  
### Response
```
Content type = application/hal+json
Body = {"_embedded":
 {"upbitTickResourceList":
   [
      {"code":"KRW-ADA",
       "type":"sell",
       "tradePrice":10.0,
        "tradeVolume":100.0,
        "askBid":"ask",
        "prevClosingPrice":10.0,
        "change":"change",
        "changePrice":10.0,
        "tradeDateUtc":"2022-12-14",
        "tradeTimeUtc":"15:21:48",
        "timestamp":1670252400999,
        "streamType":"soc_stream"}]},
"_links":
  {"first":{"href":"http://localhost:8080/cdrapi/ticks/upbit/KRW-ADA?page=0&size=2&sort=timestamp,desc"},
  "self":{"href":"http://localhost:8080/cdrapi/ticks/upbit/KRW-ADA?page=0&size=2&sort=timestamp,desc"},
  "next":{"href":"http://localhost:8080/cdrapi/ticks/upbit/KRW-ADA?page=1&size=2&sort=timestamp,desc"},
  "last":{"href":"http://localhost:8080/cdrapi/ticks/upbit/KRW-ADA?page=499&size=2&sort=timestamp,desc"},
  "profile":{"href":"docs/index.html"}},
"page":{"size":2,"totalElements":1000,"totalPages":500,"number":0}}
```

    
    
# TODO
1. 추가: logging

2. 수정:가독성이 안좋아 ticks 의 종류에 따라 엔드포인트를 나눌 예정 (~12/20)


    
