package com.ruqimobility.precisiontest.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author lj
 * @title PrecisionPlanReq
 * @description
 * @create 2023/7/11 15:01
 **/
@Data
@AllArgsConstructor
public class PrecisionPlanReq {

    private String projectId;

    /**
     * 风险等级：0-低、1-高风险、2-未开始；
     */
    private Integer riskLevel;

    private String orderCode;

    private String storyIds;

    private String startTime;

    private String endTime;

    private Integer pageNum;
    
    private Integer pageSize;
}
