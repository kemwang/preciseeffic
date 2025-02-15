package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

/**
 * @author lj
 * @title OpenApiConflictResp
 * @description
 * @create 2023/10/11 18:25
 **/
@Data
public class OpenApiConflictDto {
    private String projectName;
    private String projectId;
    private String storyIds;
    private String path;
    private String pathName;
    private String orderCode;
    private Long taskId;
}
