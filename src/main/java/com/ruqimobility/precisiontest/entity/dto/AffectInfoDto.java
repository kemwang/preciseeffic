package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

@Data
public class AffectInfoDto {
    private String orderCode;
    private Integer affectMethods;
    private Integer affectCodeLines;
    private Integer affectCallChain;
}
