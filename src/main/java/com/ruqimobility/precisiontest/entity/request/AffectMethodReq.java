package com.ruqimobility.precisiontest.entity.request;

import lombok.Data;

/**
 * TODO
 *
 * @author liujia
 * @date 5/9 18:38
 */
@Data
public class AffectMethodReq extends SummaryStatisticsReq{
    private String methodName;
    private Integer pageNum;
    private Integer pageSize;
    public AffectMethodReq(Long taskId, Long taskInfoId, String methodName, Integer pageNum, Integer pageSize) {
        super(taskId, taskInfoId);
        this.methodName = methodName;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
