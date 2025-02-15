package com.ruqimobility.precisiontest.entity.request;

import lombok.Data;

@Data
public class CodeDiffTaskInfoReq {
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * gitlab项目id
     */
    private String projectId;

    private String orderId;

    /**
     * 代码基准分支名称
     */
    private String baseBranch;

    /**
     * 基于基准分支的commitId
     */
    private String baseCommitId;

    /**
     * 基于基准分支的commit内容
     */
    private String baseCommitContent;

    /**
     * 对比分支名称
     */
    private String compareBranch;


    /**
     * 对比分支ID
     */

    private String compareCommitId;

    /**
     * 对比分支的commit内容
     */
    private String compareCommitContent;

    private String deployBranch;
    /**
     * 项目URL
     */
    private String projectUrl;

    private String taskRelationId;

    private String extra;

    public void validate(){
        if (projectName == null || projectName.isEmpty()) {
            throw new IllegalArgumentException("项目名称不能为空");
        }
        if (projectId == null || projectId.isEmpty()) {
            throw new IllegalArgumentException("gitlab项目id不能为空");
        }
        if (baseBranch == null || baseBranch.isEmpty()) {
            throw new IllegalArgumentException("代码基准分支名称不能为空");
        }
        if (baseCommitId == null || baseCommitId.isEmpty()) {
            throw new IllegalArgumentException("基于基准分支的commitId不能为空");
        }
        if (compareBranch == null || compareBranch.isEmpty()) {
            throw new IllegalArgumentException("对比分支名称不能为空");
        }
        if (compareCommitId == null || compareCommitId.isEmpty()) {
            throw new IllegalArgumentException("对比分支commitID不能为空");
        }
    }

}
