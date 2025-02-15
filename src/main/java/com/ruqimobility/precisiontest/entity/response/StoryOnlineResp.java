package com.ruqimobility.precisiontest.entity.response;

import com.ruqimobility.precisiontest.service.impl.CodeDiffTaskServiceImpl;
import com.ruqimobility.precisiontest.service.impl.StoryInfoServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author lj
 * @title StoryOnlineResp
 * @description
 * @create 2023/11/16 16:55
 **/
@Data
public class StoryOnlineResp {
    private int total;
    private int type;
    private Map<String, CodeDiffTaskServiceImpl.PrecisionSummary> datas;
    @Data
    public class onlineData{
        public String date;
        public int count;
    }

}
