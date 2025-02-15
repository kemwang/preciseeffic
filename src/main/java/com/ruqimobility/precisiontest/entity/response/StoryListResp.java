package com.ruqimobility.precisiontest.entity.response;

import com.ruqimobility.precisiontest.entity.persistent.OdsStory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author lj
 * @title StoryIdListResp
 * @description
 * @create 2023/11/16 15:22
 **/
@Data
@AllArgsConstructor
public class StoryListResp {
    private int total;
    private List<OdsStory> odsStories;
}
