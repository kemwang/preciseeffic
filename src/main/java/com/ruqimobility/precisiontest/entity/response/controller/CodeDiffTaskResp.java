package com.ruqimobility.precisiontest.entity.response.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruqimobility.precisiontest.entity.persistent.CodeDiffTaskInfo;
import com.ruqimobility.precisiontest.entity.request.CodeDiffTaskInfoReq;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CodeDiffTaskResp {
    private Long id;

    /**
     * 任务名称，对应手动输入或提测单名称
     */
    private String taskName;

    /**
     * 祺效提测单id
     */
    private String orderId;

    /**
     * 祺效提测单code
     */
    private String orderCode;

    /**
     * 多个用逗号分割
     */
    private String storyIds;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 当前进度；进度枚举待定
     */
    private Integer progress;

    private String tapdProjectId;

    private String tapdProjectName;

    private String storyUrl;

    private String groupId;

    private Boolean type;

    private String storyInfo;


    /**
     * 备用字段
     */
    private String extra;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private List<CodeDiffTaskInfo> codeDiffTaskInfos;



}
