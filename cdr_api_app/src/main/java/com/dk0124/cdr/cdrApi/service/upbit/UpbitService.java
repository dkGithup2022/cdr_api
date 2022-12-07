package com.dk0124.cdr.cdrApi.service.upbit;

import com.dk0124.cdr.constants.coinCode.UpbitCoinCode.UpbitCoinCode;
import com.dk0124.cdr.persistence.dto.upbit.candle.UpbitCandleDto;
import com.dk0124.cdr.persistence.dto.upbit.orderbook.UpbitOrderbookDto;
import com.dk0124.cdr.persistence.dto.upbit.tick.UpbitTickDto;
import com.dk0124.cdr.persistence.repository.upbit.upbitCandleRepository.UpbitCandleCommonJpaInterface;
import com.dk0124.cdr.persistence.repository.upbit.upbitOrderBookRepository.UpbitOrderbookCommonRepository;
import com.dk0124.cdr.persistence.repository.upbit.upbitTickRepository.UpbitTickRepository;
import com.dk0124.cdr.persistence.repositoryPicker.upbit.UpbitCandleRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.upbit.UpbitOrderbookRepositoryPicker;
import com.dk0124.cdr.persistence.repositoryPicker.upbit.UpbitTickRepositoryPicker;
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
    private final UpbitTickRepositoryPicker upbitTickRepositoryPicker;
    private final UpbitOrderbookRepositoryPicker upbitOrderbookRepositoryPicker;
    private final UpbitCandleRepositoryPicker upbitCandleRepositoryPicker;

    private final ModelMapper modelMapper;

    public Page<UpbitTickDto> getUpbitTicksBefore(UpbitCoinCode code, Long timestamp, int size, int page) {
        UpbitTickRepository repo = upbitTickRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest).map(d -> modelMapper.map(d, UpbitTickDto.class));
    }

    public Page<UpbitOrderbookDto> getUpbitOrderbooksBefore(UpbitCoinCode code, Long timestamp, int size, int page) {
        UpbitOrderbookCommonRepository repo = upbitOrderbookRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest).map(d -> modelMapper.map(d, UpbitOrderbookDto.class));
    }

    public Page<UpbitCandleDto> getUpbitCandlesBefore(UpbitCoinCode code, Long timestamp, int size, int page) {
        UpbitCandleCommonJpaInterface repo = upbitCandleRepositoryPicker.getRepositoryFromCode(code);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return repo.findByTimestampLessThanEqual(timestamp, pageRequest).map(d -> modelMapper.map(d, UpbitCandleDto.class));
    }


}
