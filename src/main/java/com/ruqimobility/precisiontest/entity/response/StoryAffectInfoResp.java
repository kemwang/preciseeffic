package com.ruqimobility.precisiontest.entity.response;

import lombok.Data;

@Data
public class StoryAffectInfoResp {
    String yearMonth;
    Long taskId;
    String taskName;
    String orderCode;
    String storyIds;
    Integer affectCallChain;
    Integer affectCodeLines;
}
