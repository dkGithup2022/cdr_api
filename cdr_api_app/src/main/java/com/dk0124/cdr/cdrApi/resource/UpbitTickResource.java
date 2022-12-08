package com.dk0124.cdr.cdrApi.resource;

import com.dk0124.cdr.persistence.dto.upbit.tick.UpbitTickDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


public class UpbitTickResource extends RepresentationModel {
    @Getter
    @Setter
    @JsonUnwrapped
    private UpbitTickDto item;

    public UpbitTickResource(UpbitTickDto upbitTickDto){
        this.item = upbitTickDto;
    }
}
