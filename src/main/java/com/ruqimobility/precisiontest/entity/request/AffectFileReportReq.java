package com.ruqimobility.precisiontest.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO
 *
 * @author liujia
 * @date 5/9 18:38
 */
@Data
public class AffectFileReportReq extends SummaryStatisticsReq{
    private String filename;
    private String fileType;
    private Integer pageNum;
    private Integer pageSize;
    public AffectFileReportReq(Long taskId, Long taskInfoId, String filename, String fileType, Integer pageNum, Integer pageSize) {
        super(taskId, taskInfoId);
        this.filename = filename;
        this.fileType = fileType;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
