package com.ruqimobility.precisiontest.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.ruqimobility.precisiontest.codeanalyzer.GitlabAnalyzer;
import com.ruqimobility.precisiontest.entity.dto.CloneProjectDto;
import com.ruqimobility.precisiontest.entity.dto.DownloadJarDto;
import com.ruqimobility.precisiontest.entity.persistent.CodeDiffTask;
import com.ruqimobility.precisiontest.entity.persistent.CodeDiffTaskInfo;
import com.ruqimobility.precisiontest.entity.persistent.QxJarInfo;
import com.ruqimobility.precisiontest.entity.persistent.TaskPipeline;
import com.ruqimobility.precisiontest.entity.request.CodeDiffTaskInfoReq;
import com.ruqimobility.precisiontest.entity.request.CodeDiffTaskReq;
import com.ruqimobility.precisiontest.entity.request.CompareByBranchReq;
import com.ruqimobility.precisiontest.mapper.CodeDiffTaskInfoMapper;
import com.ruqimobility.precisiontest.mapper.CodeDiffTaskMapper;
import com.ruqimobility.precisiontest.service.*;
import org.gitlab.api.models.GitlabCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.ruqimobility.precisiontest.constants.PipelineConstant.DEPLOY_DONE;

/**
 * TODO
 *
 * @author liujia
 * @date 5/19 16:40
 */
@Component
public class CreateTaskFlow {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTaskFlow.class);
    @Resource
    CodeDiffTaskInfoMapper taskInfoMapper;
    @Resource
    CodeAnalysisFacade codeAnalysisFacade;
    @Resource
    GitlabAnalyzer gitlabAnalyzer;

    @Resource
    CallGraphService callGraphService;

    @Resource
    QxJarInfoService qxJarInfoService;

    @Resource
    CodeDiffTaskMapper codeDiffTaskMapper;

    @Async
    public void start(Long taskId,Long serviceId) throws IOException, ExecutionException, InterruptedException {
        QueryWrapper<CodeDiffTaskInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_relation_id", String.valueOf(taskId));
        if(serviceId!=null){
            queryWrapper.eq("id",serviceId);
        }
        List<CodeDiffTaskInfo> codeDiffTaskInfos = taskInfoMapper.selectList(queryWrapper);
        List<CloneProjectDto> cloneProjects = new ArrayList<>();
        Set<DownloadJarDto> dtoSet = new HashSet<>();
        for (CodeDiffTaskInfo taskInfo : codeDiffTaskInfos) {
            String projectId = taskInfo.getProjectId();
            String projectName = taskInfo.getProjectName();

            //运行代码分析、报告流程
            TaskPipeline taskPipeline = new TaskPipeline();
            taskPipeline.setTaskId(taskId);
            taskPipeline.setGitProjectId(projectId);
            taskPipeline.setGitProjectName(projectName);
            String baseCommitId = taskInfo.getBaseCommitId();
            String compareCommitId = taskInfo.getCompareCommitId();
            CompareByBranchReq compareByBranchReq = new CompareByBranchReq();
            compareByBranchReq.setProjectName(projectName);
            compareByBranchReq.setBaseCommit(baseCommitId);
            compareByBranchReq.setCompareCommit(compareCommitId);
            compareByBranchReq.setTaskPipeline(taskPipeline);
            compareByBranchReq.setTaskId(taskId);
            compareByBranchReq.setProjectId(projectId);
            //分析对应的application名称
            gitlabAnalyzer.recordAppName(compareByBranchReq);
            //分析分支差异
            gitlabAnalyzer.compareByBranch(compareByBranchReq);
            //生成分支差异报告
            codeAnalysisFacade.codeAnalysisReport(taskId,taskInfo.getId(),taskInfo);

            CodeDiffTask codeDiffTask = codeDiffTaskMapper.selectById(taskId);
            QxJarInfo qxJarInfo = null;
            //判断创建任务来源，如果是手动创建，则处理
            if(codeDiffTask.getOrderCode()==null){
                //查询祺效包等相关信息
                qxJarInfo = qxJarInfoService.getQxJarInfo(projectId,taskInfo.getCompareBranch(), taskInfo.getCompareCommitId());
            }
            else{
                qxJarInfo = qxJarInfoService.getQxJarInfoByOrderAndOther(Integer.parseInt(codeDiffTask.getOrderId()),
                        projectId,taskInfo.getCompareBranch(), taskInfo.getCompareCommitId());
            }
            if(qxJarInfo != null){
                DownloadJarDto dto = new DownloadJarDto();
                dto.setProjectId(taskInfo.getProjectId());
                dto.setTaskId(taskId);
                dto.setBranch(taskInfo.getCompareBranch());
                dto.setUrl(qxJarInfo.getDownloadJarUrl());
                dto.setDir(qxJarInfo.getDir());
                dtoSet.add(dto);
                //构建clone项目实体
                CloneProjectDto cloneProject = new CloneProjectDto();
                cloneProject.setTaskId(taskId);
                cloneProject.setGitProjectId(projectId);
                cloneProject.setBranchName(taskInfo.getCompareBranch());
                cloneProject.setGitUrl(qxJarInfo.getGitHttpUrl());
                cloneProject.setGitProjectName(projectName);
                cloneProjects.add(cloneProject);
            } else {
                LOGGER.info("task_id: {}, 项目名 {}, 未查询到jar包信息", taskId, projectName);
            }
        }
        if(!dtoSet.isEmpty()) {
            callGraphService.downloadJar(dtoSet);
        }
        if(!cloneProjects.isEmpty()){
            gitlabAnalyzer.gitClone(cloneProjects);
        }
    }



    public void codeAnalyzerAndReport(CodeDiffTaskInfo taskInfo) throws IOException, ExecutionException, InterruptedException {
        TaskPipeline taskPipeline = new TaskPipeline();
        Long taskId = Long.valueOf(taskInfo.getTaskRelationId());
        taskPipeline.setTaskId(taskId);
        taskPipeline.setGitProjectId(taskInfo.getProjectId());
        taskPipeline.setGitProjectName(taskInfo.getProjectName());
        String baseCommitId = taskInfo.getBaseCommitId();
        String compareCommitId = taskInfo.getCompareCommitId();
        String projectName = taskInfo.getProjectName();
        CompareByBranchReq compareByBranchReq = new CompareByBranchReq();
        compareByBranchReq.setProjectName(projectName);
        compareByBranchReq.setBaseCommit(baseCommitId);
        compareByBranchReq.setCompareCommit(compareCommitId);
        compareByBranchReq.setTaskPipeline(taskPipeline);
        compareByBranchReq.setTaskId(taskId);
        compareByBranchReq.setProjectId(taskInfo.getProjectId());
        //分析分支差异
        gitlabAnalyzer.compareByBranch(compareByBranchReq);
        //生成分支差异报告
        codeAnalysisFacade.codeAnalysisReport(taskId,taskInfo.getId(),taskInfo);
    }
}
