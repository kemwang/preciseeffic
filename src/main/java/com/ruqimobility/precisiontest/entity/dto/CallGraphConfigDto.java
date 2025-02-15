package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Data
public class CallGraphConfigDto {
    /**
     * 关联精准测试任务的id
     */
    private Long taskId;
    private Long taskInfoId;
    private String gitProjectId;
    private String gitProjectName;
    private String branch;
    private String jarPath;
    private String appName;
    private String outPutPath;
    /**
     * 要处理的类名前缀,例如：
     *  "com.ruqimobility.casemanagement",
     *   "java."
     */
    Set<String> allowedClassPrefix;

    /**
     * 向上查找调用链时配置的类名，可带上方法，例如：
     * "WebSocket:getRoom(",
     * "ReportServiceImpl:getOA(",
     * "ReportServiceImpl:updateReportContent("
     */
    List<String> calleeClassMethodName;

    /**
     * 向下查找调用链时配置的类名，格式同上
     */
    List<String> callerClassMethodName;

    /**
     * 微服务调用链MAP
     */
    HashMap<Long,String> appMap;
}
