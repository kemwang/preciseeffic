package com.ruqimobility.precisiontest.entity.response;

import lombok.Data;

import java.util.Date;

/**
 * @author lj
 * @title LatestTaskResp
 * @description
 * @create 2023/9/21 11:27
 **/
@Data
public class LatestTaskResp {
    private long taskId;
    private String storyId;
    private String orderCode;
    private Date updateTime;
}
