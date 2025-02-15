package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

/**
 * @author lj
 * @title test
 * @description
 * @create 2023/11/21 12:48
 **/
@Data
public class PrecisionSummary {
    int affectCodeLines;
    int affectCallChain;
    int affectInterface;
    public PrecisionSummary(){
        this.affectCodeLines=0;
        this.affectInterface=0;
        this.affectCallChain=0;
    }
    public void accumulate(int affectCodeLines, int affectInterface, int affectCallChain) {
        affectCodeLines += affectCodeLines;
        affectInterface += affectInterface;
        affectCallChain += affectCallChain;
    }
    public void combine(PrecisionSummary other) {
        this.affectCodeLines += other.affectCodeLines;
        this.affectInterface += other.affectInterface;
        this.affectCallChain += other.affectCallChain;
    }

}