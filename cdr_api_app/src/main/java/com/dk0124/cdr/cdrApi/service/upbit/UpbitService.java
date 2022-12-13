package com.dk0124.cdr.cdrApi.service.upbit;

import com.dk0124.cdr.cdrApi.resource.UpbitTickResource;
import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.persistence.dto.upbit.candle.UpbitCandleDto;
import com.dk0124.cdr.persistence.dto.upbit.orderbook.UpbitOrderbookDto;
import com.dk0124.cdr.persistence.dto.upbit.tick.UpbitTickDto;
import com.dk0124.cdr.persistence.repository.upbit.upbitCandleRepository.UpbitCandleRepository;
import com.dk0124.cdr.persistence.repository.upbit.upbitOrderBookRepository.UpbitOrderbookRepository;
import com.dk0124.cdr.persistence.repository.upbit.upbitTickRepository.UpbitTickRepository;
import com.dk0124.cdr.persistence.repositoryUtils.upbit.UpbitCandleRepositoryUtils;
import com.dk0124.cdr.persistence.repositoryUtils.upbit.UpbitOrderbookRepositoryUtils;
import com.dk0124.cdr.persistence.repositoryUtils.upbit.UpbitTickRepositoryUtils;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//TODO : 리팩토링 사항, 중복 제거, 호출 함수 이쁘게  따로 만들기
public class UpbitService {
    private final UpbitTickRepositoryUtils upbitTickRepositoryUtils;
    private final UpbitOrderbookRepositoryUtils upbitOrderbookRepositoryUtils;
    private final UpbitCandleRepositoryUtils upbitCandleRepositoryUtils;

    private final ModelMapper modelMapper;

    public Page<UpbitTickResource> getUpbitTicksBefore(UpbitCoinCode code, Long timestamp, int size, int page) {
        UpbitTickRepository repo = upbitTickRepositoryUtils.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest)
                .map(d -> {
                    UpbitTickDto dto = modelMapper.map(d,UpbitTickDto.class);
                    return new UpbitTickResource(dto);
                }
                );
    }

    public Page<UpbitOrderbookDto> getUpbitOrderbooksBefore(UpbitCoinCode code, Long timestamp, int size, int page) {
        UpbitOrderbookRepository repo = upbitOrderbookRepositoryUtils.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest).map(d -> modelMapper.map(d, UpbitOrderbookDto.class));
    }

    public Page<UpbitCandleDto> getUpbitCandlesBefore(UpbitCoinCode code, Long timestamp, int size, int page) {
        UpbitCandleRepository repo = upbitCandleRepositoryUtils.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest).map(d -> modelMapper.map(d, UpbitCandleDto.class));
    }


}
