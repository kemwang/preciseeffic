package com.ruqimobility.precisiontest.entity.response;

import lombok.Data;

/**
 * @author lvzhiqi
 * @date 2023/4/28
 */
@Data
public class JarInfoMessageResp {
    private String env;
    private String gitProjectId;
    private String gitHttpUrl;
    private String branch;
    private String commitId;
    private Integer applyOrderId;
    private String downloadJarUrl;
}
