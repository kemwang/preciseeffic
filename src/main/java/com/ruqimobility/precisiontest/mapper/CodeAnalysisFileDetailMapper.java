package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.CodeAnalysisFileDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.persistent.CodeAnalysisFileDetailJoinProjectBlacklist;
import com.ruqimobility.precisiontest.entity.request.AffectFileReportReq;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liujia
 * @since 2023-05-06
 */
@Repository
public interface CodeAnalysisFileDetailMapper extends BaseMapper<CodeAnalysisFileDetail> {
    List<CodeAnalysisFileDetailJoinProjectBlacklist> searchFiles(AffectFileReportReq reportReq);

    int replaceInto(CodeAnalysisFileDetail codeAnalysisFileDetail);

}
