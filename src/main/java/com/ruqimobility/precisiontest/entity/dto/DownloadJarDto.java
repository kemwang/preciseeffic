package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

@Data
public class DownloadJarDto {
    private Long taskId;
    private String projectId;
    private String branch;
    private String url;
    private String dir;
}
