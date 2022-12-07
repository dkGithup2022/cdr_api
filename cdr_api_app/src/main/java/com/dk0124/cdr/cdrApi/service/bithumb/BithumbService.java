package com.dk0124.cdr.cdrApi.service.bithumb;

import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.persistence.entity.bithumb.candle.BithumbCandle;
import com.dk0124.cdr.persistence.entity.bithumb.orderbook.BithumbOrderbook;
import com.dk0124.cdr.persistence.entity.bithumb.tick.BithumbTick;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbCandleRepository.BithumbCandleCommonJpaInterface;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbOrderbookRepository.BithumbOrderbookCommonJpaInterface;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbTickRepository.BithumbTickCommonJpaInterface;
import com.dk0124.cdr.persistence.repositoryPicker.bithumb.BithumbCandleRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.bithumb.BithumbOrderbookRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.bithumb.BithumbTickRepositoryPicker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
//TODO : 리팩토링 사항, 중복 제거, 호출 함수 이쁘게 따로 만들기
public class BithumbService {
    private final BithumbTickRepositoryPicker bithumbTickRepositoryPicker;
    private final BithumbCandleRepositoryPicker bithumbCandleRepositoryPicker;
    private final BithumbOrderbookRepositoryPicker bithumbOrderbookRepositoryPicker;

    public Page<BithumbCandle> getBithumbCandlesBefore(BithumbCoinCode code, Long timestamp, int size) {
        BithumbCandleCommonJpaInterface repo = bithumbCandleRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest);
    }

    public Page<BithumbCandle> getBithumbCandlesBefore(BithumbCoinCode code, Long timestamp, int size, int page) {
        BithumbCandleCommonJpaInterface repo = bithumbCandleRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest);
    }

    public Page<BithumbOrderbook> getBithumbOrderbooksBefore(BithumbCoinCode code, Long timestamp, int size) {
        BithumbOrderbookCommonJpaInterface repo = bithumbOrderbookRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("timestamp").descending());
        return repo.findByDatetimeLessThanEqual(timestamp, pageRequest);
    }

    public Page<BithumbOrderbook> getBithumbOrderbooksBefore(BithumbCoinCode code, Long timestamp, int size, int page) {
        BithumbOrderbookCommonJpaInterface repo = bithumbOrderbookRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByDatetimeLessThanEqual(timestamp, pageRequest);
    }

    public Page<BithumbTick> getBithumbticksBefore(BithumbCoinCode code, Long timestamp, int size) {
        BithumbTickCommonJpaInterface repo = bithumbTickRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest);
    }

    public Page<BithumbTick> getBithumbticksBefore(BithumbCoinCode code, Long timestamp, int size, int page) {
        BithumbTickCommonJpaInterface repo = bithumbTickRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest);
    }

}