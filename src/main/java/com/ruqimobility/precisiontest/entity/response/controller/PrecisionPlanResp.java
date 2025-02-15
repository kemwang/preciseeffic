package com.ruqimobility.precisiontest.entity.response.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author lj
 * @title PrecisionPlanResp
 * @description 精准测试周期统计
 * @create 2023/7/10 15:28
 **/

@Data
public class PrecisionPlanResp {
    private Long id;

    private String tapdProjectName;

    private String tapdProjectId;

    private String storyInfo;

    private Integer extra;

    private Integer affectFiles;

    private Integer affectConfigFiles;

    /**
     * 受影响的方法数
     */
    private Integer affectMethods;

    /**
     * 总共变更的文件行数，暂定= 新增+删除
     */
    private Integer affectCodeLines;

    /**
     * 受影响的接口
     */
    private Integer affectInterface;

    /**
     * 受影响的调用链数量
     */
    private Integer affectCallChain;

    /**
     * 风险等级：0-低、1-中、2-高；
     */
    private Integer riskLevel;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
