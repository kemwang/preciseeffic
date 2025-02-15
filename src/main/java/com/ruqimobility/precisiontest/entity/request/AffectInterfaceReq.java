package com.ruqimobility.precisiontest.entity.request;

import lombok.Data;

/**
 * TODO
 *
 * @author liujia
 * @date 5/9 18:38
 */
@Data
public class AffectInterfaceReq extends SummaryStatisticsReq{
    private String InterfaceName;
    private String path;
    private Integer pageNum;
    private Integer pageSize;
    public AffectInterfaceReq(Long taskId, Long taskInfoId, String InterfaceName, String path, Integer pageNum, Integer pageSize) {
        super(taskId, taskInfoId);
        this.InterfaceName = InterfaceName;
        this.path = path;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
