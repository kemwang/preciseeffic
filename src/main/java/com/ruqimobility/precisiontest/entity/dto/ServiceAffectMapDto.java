package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @author lj
 * @title ServiceAffectMapDto
 * @description
 * @create 2024/1/18 11:19
 **/
@Data
public class ServiceAffectMapDto {
    private int calledHost;
    private int httpCount;
    private HashMap<String, List<ServiceAffectDto>> affectMap;
}
