package com.ruqimobility.precisiontest.entity.response.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class TaskByCodeResp {
    private Long id;

    /**
     * 任务名称，对应手动输入或提测单名称
     */
    private String taskName;


    /**
     * 祺效提测单编号
     */
    private String orderCode;

    private String tapdProjectId;

    private String tapdProjectName;

    private String storyUrl;

    private String storyInfo;

    private int taskCount;

}
