package com.ruqimobility.precisiontest.entity.response.controller;

import com.ruqimobility.precisiontest.entity.persistent.CodeDiffTaskInfo;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author liujia
 * @date 5/9 16:07
 */
@Data
public class SummaryStatisticsResp {
    private long taskId;
    //服务数量
    private int serviceCount;
    //返回服务对象列表
    private List<CodeDiffTaskInfo> codeDiffTaskInfos;
    //影响文件数量
    private int affectFileCount;
    //影响的配置文件数量
    private int affectConfigFileCount;
    //影响的方法
    private int affectMethodCount;

    //影响的代码行数
    private int affectCodeLines;

    //影响新增的代码行数
    private int affectCodeAddLines;

    //影响新增的代码行数
    private int affectCodeDelLines;

    //影响接口数
    private int affectInterface;

    //影响的调用链数
    private int affectCallChain;

    //影响的风险等级
    private int riskLevel;

    //影响中间件消息
    private int affectMessage;

    //影响定时任务
    private int affectSchedule;

}
