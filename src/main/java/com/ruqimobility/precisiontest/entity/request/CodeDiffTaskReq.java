package com.ruqimobility.precisiontest.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class CodeDiffTaskReq {


    private String taskName;
    /**
     * 祺效提测单id
     */
    private String orderId;

    /**
     * 祺效提测单code
     */
    private String orderCode;

    private String storyIds;

    private String storyInfo;

    private String tapdProjectId;

    private String tapdProjectName;

    private String storyUrl;

    private String creator;

    private Boolean type;

//  private String groupId;

    private List<CodeDiffTaskInfoReq> codeDiffTaskInfoReqs;

    public void validate(){
        if (taskName == null || taskName.isEmpty()) {
            throw new IllegalArgumentException("任务名称不能为空");
        }
        if (codeDiffTaskInfoReqs == null || codeDiffTaskInfoReqs.isEmpty()) {
            throw new IllegalArgumentException("任务信息不能为空");
        }
        for (CodeDiffTaskInfoReq codeDiffTaskInfoReq : codeDiffTaskInfoReqs) {
            codeDiffTaskInfoReq.validate();
        }
    }


}
