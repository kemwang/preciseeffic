package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

@Data
public class CloneProjectDto {

    private Long taskId;

    private String gitProjectId;

    private String gitProjectName;

    private String branchName;

    private String gitUrl;
}
