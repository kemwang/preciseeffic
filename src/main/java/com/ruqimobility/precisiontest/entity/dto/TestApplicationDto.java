package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

/**
 * @author lvzhiqi
 * @date 2023/8/18
 */
@Data
public class TestApplicationDto {
    private String deployBranch;
    private String developBranch;
    private Boolean existCloudConfig;
    private Boolean existRelyApplication;
    private Boolean existSql;
    private Long gitlabProjectId;
}
