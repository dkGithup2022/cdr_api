package com.dk0124.cdr.cdrApi.service.bithumb;

import com.dk0124.cdr.constants.coinCode.bithumbCoinCode.BithumbCoinCode;
import com.dk0124.cdr.persistence.dto.bithumb.candle.BithumbCandleDto;
import com.dk0124.cdr.persistence.dto.bithumb.orderbook.BithumbOrderbookDto;
import com.dk0124.cdr.persistence.entity.bithumb.tick.BithumbTick;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbCandleRepository.BithumbCandleRepository;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbOrderbookRepository.BithumbOrderbookRepository;
import com.dk0124.cdr.persistence.repository.bithumb.bithumbTickRepository.BithumbTickRepository;
import com.dk0124.cdr.persistence.repositoryUtils.bithumb.BithumbCandleRepositoryUtils;
import com.dk0124.cdr.persistence.repositoryUtils.bithumb.BithumbOrderbookRepositoryUtils;
import com.dk0124.cdr.persistence.repositoryUtils.bithumb.BithumbTickRepositoryUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
//TODO : 리팩토링 사항, 중복 제거, 호출 함수 이쁘게 따로 만들기
public class BithumbService {
    private final BithumbTickRepositoryUtils bithumbTickRepositoryUtils;
    private final BithumbCandleRepositoryUtils bithumbCandleRepositoryUtils;
    private final BithumbOrderbookRepositoryUtils bithumbOrderbookRepositoryUtils;
    private final ModelMapper modelMapper;

    public Page<BithumbCandleDto> getBithumbCandlesBefore(BithumbCoinCode code, Long timestamp, int size, int page) {
        BithumbCandleRepository repo = bithumbCandleRepositoryUtils.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest).map(d->modelMapper.map(d,BithumbCandleDto.class));
    }

    public Page<BithumbOrderbookDto> getBithumbOrderbooksBefore(BithumbCoinCode code, Long timestamp, int size, int page) {
        BithumbOrderbookRepository repo = bithumbOrderbookRepositoryUtils.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByDatetimeLessThanEqual(timestamp, pageRequest).map(d->modelMapper.map(d,BithumbOrderbookDto.class));
    }

    public Page<BithumbTick> getBithumbticksBefore(BithumbCoinCode code, Long timestamp, int size, int page) {
        BithumbTickRepository repo = bithumbTickRepositoryUtils.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest);
    }

}