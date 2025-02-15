package com.ruqimobility.precisiontest.entity.response;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author lvzhiqi
 * @Date 2023/4/28
 */
@Data
public class TaskPipelineInfoResp {
    private Long taskId;
    /**
     * 0未开始，1已完成
     */
    private Integer deployStatus;

    /**
     * 0未开始，1已完成
     */
    private Integer gitcloneStatus;

    /**
     * 0未开始，1已完成 （由于涉及多模块，部分模块未下载完成，则状态为9）
     */
    private Integer downloadJarStatus;

    /**
     * 0未开始，1已完成
     */
    private Integer codeDiffStatus;

    /**
     * 0未开始，1已完成
     */
    private Integer callgraphStatus;

    /**
     * 0未开始，1已完成
     */
    private Integer resultStatus;

}
