package com.ruqimobility.precisiontest.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.Patch;
import com.ruqimobility.precisiontest.aop.MonitorTime;
import com.ruqimobility.precisiontest.constants.GitlabConstant;
import com.ruqimobility.precisiontest.entity.persistent.CodeDiffResult;
import com.ruqimobility.precisiontest.entity.persistent.CodeDiffTaskInfo;
import com.ruqimobility.precisiontest.entity.persistent.TaskPipeline;
import com.ruqimobility.precisiontest.entity.request.CompareByBranchReq;
import com.ruqimobility.precisiontest.mapper.CodeDiffResultMapper;
import com.ruqimobility.precisiontest.codeanalyzer.GitlabAnalyzer;
import com.ruqimobility.precisiontest.codeanalyzer.JavaCodeParser;
import com.ruqimobility.precisiontest.service.CodeAnalysisFacade;
import com.ruqimobility.precisiontest.utils.StrUtils;
import org.gitlab.api.models.GitlabCommitDiff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * TODO
 * 代码静态分析异步方法集合
 * @author liujia
 * @date 3/28 18:14
 */
@Component
public class CodeAnalyzerAsyncTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeAnalyzerAsyncTask.class);
    @Autowired
    private CodeDiffResultMapper codeDiffResultMapper;

    @Autowired
    private ObjectFactory<JavaCodeParser> javaCodeParserFactory;

    GitlabAnalyzer gitlabAnalyzer = new GitlabAnalyzer();

    @Transactional(rollbackFor = Exception.class)
    public void codeDiffAsync(String pid, String baseCommit, String compareCommit, GitlabCommitDiff diff) throws IOException {
        CodeDiffResult codeDiffResult = new CodeDiffResult();
        boolean isNewFile = diff.getNewFile();
        boolean isDelFile = diff.getDeletedFile();
        boolean renamedFile = diff.getRenamedFile();
        String fileType = "other";
        String newPath = diff.getNewPath();
        String oldPath = diff.getOldPath();
        String diffs = diff.getDiff();

        if (Objects.isNull(diffs) || "".equals(diffs)) {
            LOGGER.info("文件:{}获取内容为:{},手动拉取差异文件！", newPath, diffs);
            if (isNewFile){
                try {
                    byte[] revised = gitlabAnalyzer.gitlabAPI.getRawFileContent(pid, compareCommit, newPath);
                    List<String> revisedlist = Arrays.asList(new String(revised).split("\n"));
                    List<String> originalList = new ArrayList<>();
                    diffs = StrUtils.diffGitFiles(originalList,revisedlist,"/dev/null",newPath);
                }catch (FileNotFoundException e){
                    LOGGER.info("新增文件:"+oldPath+",找不到");
                    e.printStackTrace();
                }
            } else if (isDelFile){
                try {
                    byte[] original = gitlabAnalyzer.gitlabAPI.getRawFileContent(pid, baseCommit, oldPath);
                    List<String> originalList = Arrays.asList(new String(original).split("\n"));
                    List<String> revisedlist = new ArrayList<>();
                    diffs = StrUtils.diffGitFiles(originalList,revisedlist,oldPath,"/dev/null");
                }catch (FileNotFoundException e){
                    LOGGER.info("删除文件:"+oldPath+",找不到");
                    e.printStackTrace();
                }
            } else{
                try {
                    byte[] original = gitlabAnalyzer.gitlabAPI.getRawFileContent(pid, baseCommit, oldPath);
                    List<String> originalList = Arrays.asList(new String(original).split("\n"));
                    byte[] revised = gitlabAnalyzer.gitlabAPI.getRawFileContent(pid, compareCommit, newPath);
                    List<String> revisedlist = Arrays.asList(new String(revised).split("\n"));
                    diffs = StrUtils.diffGitFiles(originalList, revisedlist, oldPath, newPath);
                }catch (FileNotFoundException e){
                    LOGGER.info("修改文件:"+oldPath+",找不到");
                    e.printStackTrace();
                }
            }
        }
        if(StrUtils.isMessyCode(diffs)){
            diffs = "unknown";
        }
        if(diffs.startsWith("@@")){
            if(isNewFile) {
                diffs = StrUtils.formatDiffs(diffs, null, newPath, 0);
            }else if (isDelFile){
                diffs = StrUtils.formatDiffs(diffs, oldPath,null,1);
            }else{
                diffs = StrUtils.formatDiffs(diffs, oldPath,newPath,2);
            }
        }
        codeDiffResult.setGitlabPid(pid);
        codeDiffResult.setBaseCommit(baseCommit);
        codeDiffResult.setCompareCommit(compareCommit);
        codeDiffResult.setIsNewfile(isNewFile);
        codeDiffResult.setCompareDiff(diffs);
        codeDiffResult.setIsDeletedfile(isDelFile);
        codeDiffResult.setFileOldPath(oldPath);
        codeDiffResult.setFileNewPath(newPath);
        codeDiffResult.setIsRenamedfile(renamedFile);
        List<String> resultReg = GitlabAnalyzer.getContentByReg(newPath, GitlabConstant.FILENAME_PARSER_REG,false);
        if(resultReg.size()>0){
            String fileName = resultReg.get(0);
            codeDiffResult.setFileName(fileName);
            //转换fileType
            fileType = GitlabAnalyzer.getFileType(fileName);
            codeDiffResult.setFileType(fileType);
        }


        if(!Objects.equals(diffs,"unknown")){
            //分析git改动影响范围
            GitlabAnalyzer.javaAnalyzer(codeDiffResult, diffs);
        }


        //暂时只分析java文件情况
        if(Objects.equals(fileType,"java")){
            JavaCodeParser javaCodeParser = javaCodeParserFactory.getObject();
            javaCodeParser.setCodeDiffResult(codeDiffResult)
                    .parse()
                    .recordAffect();
        }

        codeDiffResultMapper.resultReplace(codeDiffResult);
    }


}





