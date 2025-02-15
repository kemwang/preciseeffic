package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.CodeAnalysisInterfaceDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.persistent.CodeAnalysisInterfaceDetailAffectExt;
import com.ruqimobility.precisiontest.entity.request.AffectInterfaceReq;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liujia
 * @since 2023-05-08
 */
@Repository
public interface CodeAnalysisInterfaceDetailMapper extends BaseMapper<CodeAnalysisInterfaceDetail> {
    List<CodeAnalysisInterfaceDetail> serachInterface(AffectInterfaceReq interfaceReq);

    int replaceIntoCodeAnalysisInterfaceDetail(CodeAnalysisInterfaceDetail codeAnalysisInterfaceDetail);

    List<CodeAnalysisInterfaceDetailAffectExt> interfaceAffect(Long taskId);
}
