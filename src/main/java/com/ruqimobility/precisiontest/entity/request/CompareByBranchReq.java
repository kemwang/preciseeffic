package com.ruqimobility.precisiontest.entity.request;

import com.ruqimobility.precisiontest.entity.persistent.TaskPipeline;
import lombok.Data;

/**
 * TODO
 *
 * @author liujia
 * @date 5/8 17:28
 */
@Data
public class CompareByBranchReq {
    private long taskId;
    private String namespace;
    private String projectName;
    private String projectId;
    private String baseCommit;
    private String compareCommit;
    private TaskPipeline taskPipeline;

}
