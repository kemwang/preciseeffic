package com.ruqimobility.precisiontest.entity.request;

import lombok.Data;

/**
 * @author lj
 * @title StoryOnLineReq
 * @description
 * @create 2023/11/17 17:22
 **/
@Data
public class StoryOnLineReq {
    private int type;
    private String startDate;
    private String endDate;
}
