package com.dk0124.cdr.cdrApi.common;

import com.dk0124.cdr.constants.vendor.VendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataType {
    TICKS("ticks"),
    ORDERBOOKS("orderbooks"),
    CANDLES("candles")
   ;

    public static DataType fromString(String str){
        for(DataType type : DataType.values()){
            if(type.name.equals(str))
                return type;
        }
        return null;
    }

    public String name;
}
