package com.ruqimobility.precisiontest.entity.request;

import lombok.Data;

/**
 * @author lj
 * @title CodeContentReq
 * @description 获取代码内容请求
 * @create 2023/11/10 14:47
 **/
@Data
public class CodeFileInfo {
    private Long gitPID;
    private String compareCommit;
    private String filePath;
    private String fullPackage;
}
