package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.dto.AffectInfoDto;
import com.ruqimobility.precisiontest.entity.persistent.CodeAnalysisSummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.response.StoryAffectInfoResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liujia
 * @since 2023-05-05
 */
@Repository
public interface CodeAnalysisSummaryMapper extends BaseMapper<CodeAnalysisSummary> {
    int replaceIntoSummary(CodeAnalysisSummary cas);

    CodeAnalysisSummary totalSummary(Long taskId,Integer riskLevel);

    AffectInfoDto getAffectInfoByStory(String storyId);

    StoryAffectInfoResp getAffectInfoByStoryId(String storyId);
}
