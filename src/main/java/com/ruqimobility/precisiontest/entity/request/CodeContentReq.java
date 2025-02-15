package com.ruqimobility.precisiontest.entity.request;

import lombok.Data;

/**
 * @author lj
 * @title CodeContentReq
 * @description 获取代码内容请求
 * @create 2023/11/10 14:47
 **/
@Data
public class CodeContentReq {
    private Long gitPID;
    private String fullPackage;
    private String baseCommit;
    private String compareCommit;
    private String changeFullPackage;
    //文件类型，1-原始文件，0-修改文件;
    private int type;
}
