package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

/**
 * @author lj
 * @title OnlineStoryDto
 * @description
 * @create 2023/11/20 16:43
 **/
@Data
public class OnlineStoryDto {
    private String storyIds;
    private int affectCodeLines;
    private int affectCallChain;
    private int affectInterface;
}
