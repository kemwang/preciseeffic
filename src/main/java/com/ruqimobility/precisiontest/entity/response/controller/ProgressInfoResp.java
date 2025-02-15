package com.ruqimobility.precisiontest.entity.response.controller;

import com.ruqimobility.precisiontest.entity.persistent.TaskPipeline;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author liujia
 * @date 5/10 16:21
 */
@Data
public class ProgressInfoResp {
    private long taskId;
    private List<ProjectProgress> progressList;
    @Data
    public static class ProjectProgress{
        //taskinfo 表主键id
        private long taskInfoId;
        //git服务id
        private String gitlabPid;
        //git服务名称
        private String gitlabPname;
        //服务进度,当无进度时可能此项为空
        private TaskPipeline taskPipeline;
        public ProjectProgress(){}

    }
}
