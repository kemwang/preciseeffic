package com.ruqimobility.precisiontest.entity.response;

import lombok.Data;

@Data
public class PublishStoryAffectResp {
    private String yearMonth;
    private Integer affectCodeLines;
    private Integer affectMethods;
    private Integer storyCount;
    private Integer affectCallChain;
}
