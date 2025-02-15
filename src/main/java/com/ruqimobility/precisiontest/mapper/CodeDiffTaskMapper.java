package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.dto.OnlineStoryDto;
import com.ruqimobility.precisiontest.entity.persistent.CodeDiffTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.request.PrecisionPlanReq;
import com.ruqimobility.precisiontest.entity.dto.OpenApiConflictDto;
import com.ruqimobility.precisiontest.entity.request.TaskGroupRequest;
import com.ruqimobility.precisiontest.entity.response.controller.TaskByCodeResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liujia
 * @since 2023-04-17
 */
@Repository
public interface CodeDiffTaskMapper extends BaseMapper<CodeDiffTask> {
    List<CodeDiffTask> searchRecentRecord(@Param("storyIds") List<String> storyIds);

    List<CodeDiffTask> searchTask(PrecisionPlanReq planReq);

    List<OpenApiConflictDto> getConflictOrders(@Param("interfacePaths") List<String> interfacePaths, @Param("orderCode") String orderCode);

    List<OnlineStoryDto> getStoryAffect(@Param("storyIds") List<Long> storyIds);

    List<String> getProjectByGit(@Param("projectId") String projectId, @Param("gitName") String gitName);

    CodeDiffTask findTaskHistory(@Param("taskId") Long taskId);

    List<TaskByCodeResp> getTaskByCode(TaskGroupRequest taskGroupRequest);

}
