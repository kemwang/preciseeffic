package com.ruqimobility.precisiontest.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruqimobility.precisiontest.constants.enums.StatusCode;
import com.ruqimobility.precisiontest.entity.dto.CallGraphConfigDto;
import com.ruqimobility.precisiontest.entity.persistent.*;
import com.ruqimobility.precisiontest.exception.PrecisitionTestException;
import com.ruqimobility.precisiontest.service.*;
import com.ruqimobility.precisiontest.utils.FileUtil;
import com.ruqimobility.precisiontest.utils.HandleJarUtil;
import com.ruqimobility.precisiontest.utils.JarUtil;
import com.ruqimobility.precisiontest.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruqimobility.precisiontest.constants.CallGraphConstant.*;
import static com.ruqimobility.precisiontest.constants.GitlabConstant.*;
import static com.ruqimobility.precisiontest.constants.PipelineConstant.*;

@Component
@ConditionalOnProperty(prefix = "scheduling", name = "enabled", havingValue = "true")
public class ScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);

    @Value("${gitlab-config.jar}")
    private String jarPath;

    @Resource
    private TaskPipelineService taskPipelineService;

    @Resource
    private CallGraphService callGraphService;

    @Resource
    private CodeDiffTaskInfoService codeDiffTaskInfoService;

    @Resource
    private CodeDiffTaskService codeDiffTaskService;

    @Resource
    private CodeAnalysisSummaryService summaryService;

    @Resource
    private CallGraphTaskInfoService callGraphTaskInfoService;

    @Resource
    private TaskProjectModulesInfoService modulesInfoService;

    @Resource
    private QxJarInfoService qxJarInfoService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CodeAnalysisMethodDetailService codeAnalysisMethodDetailService;

    @Resource
    private CodeAnalysisInterfaceDetailService codeAnalysisInterfaceDetailService;

    //定时任务删除精准任务文件夹，只保留最新的100个
    @Scheduled(cron = "0 0 6 * * ? ")
    @Async("scheduledTaskExecutor")
    public void deleteDir() {
        int count = FileUtil.dirCount(jarPath);
        int reserve = 80;
        if(count >= reserve) {
            //获取当前最新任务id
            Long taskId = codeDiffTaskService.getLatestId();
            File currentDirectory = new File(jarPath);
            File[] files = currentDirectory.listFiles();
            for (File file: files){
                if(file.isDirectory() && Long.valueOf(file.getName()) < (taskId - reserve)){
                    LOGGER.info("DIR is delete, taskId:{}", file.getName());
                    FileUtil.deleteFile(jarPath + "/" + file.getName());
                }
            }
        }
    }

    @Scheduled(cron = "0 0/5 * * * ? ")
    @Async("scheduledTaskExecutor")
    public void retryCallGraph() {
        LOGGER.info("retryCallGraph定时任务触发");
        List<CallGraphTaskInfo> errorCallGraphTaskInfos = callGraphTaskInfoService.
                getErrorCallGraphTaskInfoList();
        if (!errorCallGraphTaskInfos.isEmpty()) {
            CallGraphTaskInfo errorTaskInfo = errorCallGraphTaskInfos.get(0);
            Long taskId = errorTaskInfo.getTaskId();
            TaskPipeline errorTaskPipeline = taskPipelineService.selectTaskPipeline(taskId, errorTaskInfo.getGitProjectId());
            errorTaskPipeline.setCallgraphStatus(CALLGRAPH_DOING);
            taskPipelineService.updateById(errorTaskPipeline);
            errorTaskInfo.setCallGraphStatus(UNDO);
            callGraphTaskInfoService.updateById(errorTaskInfo);
            callGraphTaskInfoService.updateRetry(errorTaskInfo.getTaskId(), errorTaskInfo.getTaskInfoId());
        }
    }

    @Scheduled(cron = "0/50 * * * * ? ")
    @Async("scheduledTaskExecutor")
    public void scheduleFindNonAffectMethodPipeline() {
        LOGGER.info("scheduleFindNonAffectMethodPipeline定时任务触发");
        List<TaskPipeline> taskPipelineList = taskPipelineService.getTaskPipelineByCodeDiffDone();
        for(TaskPipeline taskPipeline :taskPipelineList){
            CodeDiffTaskInfo taskInfo = codeDiffTaskInfoService.getTaskInfo(String.valueOf(taskPipeline.getTaskId()), taskPipeline.getGitProjectId());
            if(taskInfo != null) {
                List<CodeAnalysisMethodDetail> methodDetailList = codeAnalysisMethodDetailService.getMethodListBytask(taskPipeline.getTaskId(),taskInfo.getId());
                if(methodDetailList == null || methodDetailList.isEmpty()) {
                    taskPipeline = taskPipelineService.selectTaskPipeline(taskPipeline.getTaskId(), taskPipeline.getGitProjectId());
                    taskPipeline.setDeployStatus(DEPLOY_DONE);
                    taskPipeline.setGitcloneStatus(GITCLONE_DONE);
                    taskPipeline.setAnalysisModuleStatus(ANALYSIS_MODULE_DONE);
                    taskPipeline.setDownloadJarStatus(DOWNLOADJAR_DONE);
                    taskPipeline.setCallgraphStatus(CALLGRAPH_DONE);
                    taskPipeline.setCallgraphStatisticStatus(CALLGRAPH_STATISTIC_DONE);
                    taskPipeline.setResultStatus(RESULT_DONE);
                    CodeAnalysisSummary codeAnalysisSummary = summaryService.getSummaryInfo(taskPipeline.getTaskId(), taskInfo.getId());
                    if(Objects.nonNull(codeAnalysisSummary)) {
                        codeAnalysisSummary.setAffectInterface(0);
                        codeAnalysisSummary.setAffectCallChain(0);
                        summaryService.updateSummaryInfo(codeAnalysisSummary);
                        taskPipelineService.updateTaskPipeline(taskPipeline);
                        try {
                            taskPipelineService.sendTaskMessage(taskPipeline.getTaskId());
                        } catch (Exception e) {
                            LOGGER.error("发送飞书消息error,{}", e.getMessage());
                        }
                    }
                }
            }
        }


    }

    @Scheduled(cron = "0/35 * * * * ? ")
    @Async("scheduledTaskExecutor")
    public void scheduledInsertCallGraphInfo() {
        LOGGER.info("scheduledInsertCallGraphInfo定时任务触发");
        List<TaskPipeline> taskPipelineList = taskPipelineService.getTaskPipelineByStatusDone();
        if (!taskPipelineList.isEmpty()) {
            Set<String> allowedClassPrefix = new HashSet<>();
            List<String> effectResultList = new ArrayList<>();
            String appName;
            for (TaskPipeline task : taskPipelineList) {
                //设置分析调用链状态
                task.setCallgraphStatus(CALLGRAPH_DOING);
                taskPipelineService.updateTaskPipeline(task);
                CallGraphConfigDto dto = new CallGraphConfigDto();
                dto.setTaskId(task.getTaskId());
                List<CodeDiffTaskInfo> list = codeDiffTaskInfoService.getTaskInfoList(String.valueOf(task.getTaskId()), task.getGitProjectId());
                Set<String> affectPackageNames = new HashSet<>();
                for (CodeDiffTaskInfo info : list) {
                    dto.setTaskInfoId(info.getId());
                    dto.setGitProjectId(info.getProjectId());
                    dto.setGitProjectName(info.getProjectName());
                    appName = info.getProjectName();
                    dto.setAppName(appName);
                    dto.setBranch(info.getCompareBranch());
                    List<CodeAnalysisMethodDetail> codeAnalysisMethodDetailList = codeAnalysisMethodDetailService.getMethodListBytask(Long.valueOf(info.getTaskRelationId()), info.getId());
                    for(CodeAnalysisMethodDetail methodDetail:codeAnalysisMethodDetailList){
                        String[] packageNameArr = methodDetail.getPackageName().split("\\"+DOT);
                        if(packageNameArr.length>3) {
                            String affectPackageName = packageNameArr[0] + DOT + packageNameArr[1] + DOT + packageNameArr[2];
                            affectPackageNames.add(affectPackageName);
                            allowedClassPrefix.add(packageNameArr[0] + DOT + packageNameArr[1] + DOT + packageNameArr[2] + DOT);
                            effectResultList.add(methodDetail.getPackageName()+ DOT + methodDetail.getClassName().replaceAll(COLON, "") + COLON
                                    + methodDetail.getMethodName());
                        }
                    }
                }
                List<String> classList = effectResultList.stream().distinct().collect(Collectors.toList());
                //通过allowedClassPrefix获取模块数量，以map形式存放各模块需要获取调用链的类、方法信息
                Map<String, List<String>> moduleClassMethodMap = new HashMap<>();
                for (String packageName : allowedClassPrefix) {
                    List<String> tempClassMethodList = new ArrayList<>();
                    for (String classMethod : classList) {
                        if (classMethod.contains(packageName)) {
                            tempClassMethodList.add(classMethod);
                        }
                    }
                    moduleClassMethodMap.put(packageName.substring(0, packageName.length() - 1), tempClassMethodList);
                }
                CodeDiffTaskInfo taskInfo = codeDiffTaskInfoService.getTaskInfo(String.valueOf(task.getTaskId()), task.getGitProjectId());
                for(String affectPackageName: affectPackageNames){
                    CallGraphTaskInfo callGraphTaskInfo = new CallGraphTaskInfo();
                    callGraphTaskInfo.setTaskId(task.getTaskId());
                    callGraphTaskInfo.setTaskInfoId(taskInfo.getId());
                    callGraphTaskInfo.setGitProjectId(task.getGitProjectId());
                    callGraphTaskInfo.setGitProjectName(task.getGitProjectName());
                    callGraphTaskInfo.setBranch(taskInfo.getCompareBranch());
                    callGraphTaskInfo.setPackageName(affectPackageName);
                    callGraphTaskInfo.setAllClassMethod(StringUtils.join(moduleClassMethodMap.get(affectPackageName), SEMICOLON));
                    String path = redisUtil.get(task.getTaskId() + "_" + task.getGitProjectId());
                    String realPath = jarPath +  "/" + task.getTaskId();
                    if(!realPath.equals(path)) {
                        FileUtil.createFolder(realPath);
                        QxJarInfo qxJarInfo = qxJarInfoService.getQxJarInfoByDir(task.getGitProjectId(), path);
                        String[] tempArray = qxJarInfo.getDownloadJarUrl().split("/");
                        String jarName = tempArray[tempArray.length - 1];
                        try {
                            HandleJarUtil.copyJar(path, jarName, realPath);
                            LOGGER.info("原始包路径:{}，复制到：{}，包名：{}", path, realPath, jarName);
                        } catch (IOException e) {
                            LOGGER.error("复制包出错，{}", e.getMessage());
                            throw new RuntimeException(e);
                        }
                        //QXjarInfo写入路径，防止找不到包
                        QxJarInfo existQxJarInfo = qxJarInfoService.getQxJarInfoByDir(task.getGitProjectId(), path);
                        String dir = existQxJarInfo.getDir() + COMMA + realPath;
                        LOGGER.info("existQxJarInfo id:{},反写入QxJarInfo的dir:{}",existQxJarInfo.getId(), dir);
                        existQxJarInfo.setDir(dir);
                        path = realPath;
                    }
                    callGraphTaskInfo.setJarPath(path);
                    callGraphTaskInfo.setCallGraphStatus(UNDO);
                    callGraphTaskInfoService.replaceIntoCallGraphTaskInfo(callGraphTaskInfo);
                }
            }
        }
    }

    @Scheduled(cron = "0/15 * * * * ? ")
    @Async("scheduledTaskExecutor")
    public void scheduledRunCallGraph() {
        LOGGER.info("scheduledTaskExecutor定时任务触发");
        List<CallGraphTaskInfo> callGraphTaskDoingList = callGraphTaskInfoService.getCallGraphDoingTaskInfoList();
        List<CallGraphTaskInfo> callGraphTaskInfoList = callGraphTaskInfoService.getCallGraphTaskInfoList();
        if (callGraphTaskInfoList != null && !callGraphTaskInfoList.isEmpty() && callGraphTaskDoingList.isEmpty()) {
            LOGGER.info("scheduledTaskExecutor定时可执行任务触发");
            Set<String> allowedClassPrefix = new HashSet<>();
            CallGraphTaskInfo callGraphTaskInfo = callGraphTaskInfoList.get(0);
            callGraphTaskInfo.setCallGraphStatus(CALLGRAPH_DOING);
            callGraphTaskInfoService.updateById(callGraphTaskInfo);
            //配置影响的包名前缀
            allowedClassPrefix.add(JAVA_PREFIX);
            allowedClassPrefix.add(callGraphTaskInfo.getPackageName() + DOT);
            CallGraphConfigDto dto = new CallGraphConfigDto();
            dto.setTaskId(callGraphTaskInfo.getTaskId());
            dto.setTaskInfoId(callGraphTaskInfo.getTaskInfoId());
            dto.setGitProjectId(callGraphTaskInfo.getGitProjectId());
            dto.setGitProjectName(callGraphTaskInfo.getGitProjectName());
            dto.setBranch(callGraphTaskInfo.getBranch());
            dto.setAppName(callGraphTaskInfo.getGitProjectName());
            dto.setAllowedClassPrefix(allowedClassPrefix);
            //查询模块对应的包名关键字
            TaskProjectModulesInfo modulesInfo = modulesInfoService.getMouleInfo(callGraphTaskInfo.getTaskId()
                    , callGraphTaskInfo.getGitProjectId());
            if (modulesInfo != null) {
                List<String> modulePackageInfos = Arrays.asList(modulesInfo.getModule().split(SEMICOLON));
                String moduleName = "";
                String packageKeyword = "";
                String packageName = "";
                //查找task相关jar包下载任务是否完成
                TaskPipeline taskPipeline = taskPipelineService.selectTaskPipeline(dto.getTaskId(), dto.getGitProjectId());
//                    CodeDiffTaskInfo codeDiffTaskInfo = codeDiffTaskInfoService.getTaskInfo(String.valueOf(callGraphTaskInfo.getTaskId()), callGraphTaskInfo.getGitProjectId());
                //没有变更内容，则直接修改统计结果
                if (callGraphTaskInfo.getAllClassMethod() == null || callGraphTaskInfo.getAllClassMethod().isEmpty()) {
                    List<CallGraphTaskInfo> exsitGraphTaskInfos = callGraphTaskInfoService.getCallGraphTaskInfosByTaskId(callGraphTaskInfo.getTaskId());
                    if (exsitGraphTaskInfos != null && exsitGraphTaskInfos.size() == 1) {
                        CodeDiffTaskInfo taskInfo = codeDiffTaskInfoService.getTaskInfo(String.valueOf(taskPipeline.getTaskId()), taskPipeline.getGitProjectId());
                        CodeAnalysisSummary summary = summaryService.getSummaryInfo(taskPipeline.getTaskId(), taskInfo.getId());
                        summary.setAffectInterface(0);
                        summary.setAffectCallChain(0);
                        summaryService.updateSummaryInfo(summary);
                        taskPipeline = taskPipelineService.selectTaskPipeline(dto.getTaskId(), dto.getGitProjectId());
                        taskPipeline.setCallgraphStatus(CALLGRAPH_DONE);
                        taskPipeline.setCallgraphStatisticStatus(CALLGRAPH_STATISTIC_DONE);
                        if (taskPipeline.getCodeDiffStatisticStatus() == CODEDIFF_STATISTIC_DONE) {
                            taskPipeline.setResultStatus(RESULT_DONE);
                        }
                        taskPipelineService.updateTaskPipeline(taskPipeline);
                    }
                } else {
                    QxJarInfo downloadJarInfo = null;
                    if (taskPipeline.getDownloadJarStatus() == DOWNLOADJAR_DONE) {
                        //查找下载包名
                        downloadJarInfo = qxJarInfoService.getQxJarInfoByDir(dto.getGitProjectId(), jarPath + "/" + dto.getTaskId());
                        if(Objects.isNull(downloadJarInfo)){
//                                CodeDiffTaskInfo codeDiffTaskInfo = codeDiffTaskInfoService.getById(callGraphTaskInfo.getTaskInfoId());
                            downloadJarInfo = qxJarInfoService.getQxJarInfoByGit(dto.getGitProjectId(), dto.getBranch());
                        }
                        LOGGER.info("====getQxJarInfoByDir:{}====", downloadJarInfo);
                    } else {
                        LOGGER.info("该任务 taskid:{},getGitProjectId:{},没找到包", callGraphTaskInfo.getTaskId(), dto.getGitProjectId());
                    }
                    if (downloadJarInfo != null) {
                        String[] tempArray = downloadJarInfo.getDownloadJarUrl().split("/");
                        String downloadJarName = tempArray[tempArray.length - 1];
                        List<String> unMatchPackageKeywordList = new ArrayList<>();
                        for (String modulePackageInfo : modulePackageInfos) {
                            JSONObject info = JSON.parseObject(modulePackageInfo);
                            moduleName = info.getString("moduleName");
                            packageKeyword = info.getString("packageKeyword");
                            packageName = info.getString("packageName");
                            if (!downloadJarName.contains(packageKeyword)) {
                                unMatchPackageKeywordList.add(packageKeyword);
                            }
                            if (packageName.isEmpty()) {
                                CodeDiffTaskInfo taskInfo = codeDiffTaskInfoService.getTaskInfo(String.valueOf(modulesInfo.getTaskId()), modulesInfo.getGitProjectId());
                                CodeAnalysisSummary summary = summaryService.getSummaryInfo(modulesInfo.getTaskId(), taskInfo.getId());
                                summary.setAffectInterface(0);
                                summary.setAffectCallChain(0);
                                summaryService.updateSummaryInfo(summary);
                                taskPipeline = taskPipelineService.selectTaskPipeline(dto.getTaskId(), dto.getGitProjectId());
                                taskPipeline.setCallgraphStatus(CALLGRAPH_DONE);
                                taskPipeline.setCallgraphStatisticStatus(CALLGRAPH_STATISTIC_DONE);
                                if (taskPipeline.getCodeDiffStatisticStatus() == CODEDIFF_STATISTIC_DONE) {
                                    taskPipeline.setResultStatus(RESULT_DONE);
                                }
                                taskPipelineService.updateTaskPipeline(taskPipeline);
                            }
                        }
                        //处理多模块的包
                        if (modulePackageInfos.size() > 1 || modulePackageInfos.size() == unMatchPackageKeywordList.size()) {
                            //解压jar包
                            String jarFilePath = jarPath + "/" + dto.getTaskId() + "/" + downloadJarName;
                            JarUtil.unzipJar(jarFilePath);
                            //查找子包并合并jar包
                            for (String unMatchPackageKeyword : unMatchPackageKeywordList) {
                                String libPath = jarFilePath.replace(".jar","") + "/BOOT-INF/lib";
                                String classPath = jarFilePath.replace(".jar","") + "/BOOT-INF/classes";
                                List<String> jarList = FileUtil.searchFiles(libPath, unMatchPackageKeyword);
                                if(!jarList.isEmpty()){
                                    JarUtil.unzipJar(jarList.get(0));
                                    FileUtil.moveFolderContents(jarList.get(0).replace(".jar",""),
                                            classPath);
                                    try {
                                        JarUtil.compressDirectoryToJar(jarFilePath.replace(".jar",""), jarFilePath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        throw PrecisitionTestException.build("合并jar包出错", StatusCode.CALLGRAPH_ERROR);
                                    }
                                }
                            }
                        }
                        //任务下载目录查找相关jar包
                        List<String> jarList = FileUtil.searchFiles(callGraphTaskInfo.getJarPath(), downloadJarName);
                        if (!jarList.isEmpty()) {
                            if (jarList.size() > 1) {
                                LOGGER.error("该任务 taskid:{},包路径:{},有多个包关键字为{}的包",
                                        callGraphTaskInfo.getTaskId(), callGraphTaskInfo.getJarPath(), packageKeyword);
                            }
                            dto.setJarPath(jarList.get(0));
                            if (callGraphTaskInfo.getAllClassMethod() != null &&
                                    !callGraphTaskInfo.getAllClassMethod().isEmpty()) {
                                dto.setCalleeClassMethodName(Arrays.asList(callGraphTaskInfo.getAllClassMethod().split(SEMICOLON)));
                                dto.setOutPutPath(jarPath + "/" + dto.getTaskId());
                                try {
                                    HashMap<Long,String> appMap = codeAnalysisInterfaceDetailService.findAppMap(dto.getTaskId());
                                    dto.setAppMap(appMap);
                                    List<CallGraphTaskInfo> infos = callGraphTaskInfoService.getCallGraphTaskInfosByTaskAndTaskInfo(dto.getTaskId(), dto.getTaskInfoId());
                                    if (infos.size() > 1) {
                                        callGraphService.callGraph2Neo4j(dto, 1);
                                    } else {
                                        callGraphService.callGraph2Neo4j(dto, 0);
                                    }
                                    callGraphTaskInfoService.changeCallGraphTaskStatus(callGraphTaskInfo, CALLGRAPH_DONE);
                                } catch (Exception e) {
                                    callGraphTaskInfoService.changeCallGraphTaskStatus(callGraphTaskInfo, CALLGRAPH_ERROR);
                                    LOGGER.error("error: {}", e.getMessage());
                                    taskPipeline.setCallgraphStatus(CALLGRAPH_ERROR);
                                    taskPipelineService.updateTaskPipeline(taskPipeline);
                                }
                            } else {
                                LOGGER.info("taskid：{}, 模块名:{}, 包路径:{},暂时没有关键字为{}的包",
                                        callGraphTaskInfo.getTaskId(), moduleName, callGraphTaskInfo.getJarPath(), packageKeyword);
                            }
                        } else {
                            LOGGER.info("祺效暂时没有下发jar包消息。taskid：{}, 模块名:{}, 包路径:{}",
                                    callGraphTaskInfo.getTaskId(), moduleName, callGraphTaskInfo.getJarPath());
                        }
                    }
                }
            }
            else {
                LOGGER.error("modulesInfo is null, taskid:{}, projectid:{}",
                        callGraphTaskInfo.getTaskId(), callGraphTaskInfo.getGitProjectId());
                throw PrecisitionTestException.build("没有模块信息", StatusCode.CALLGRAPH_ERROR);
            }
        }
    }
}
