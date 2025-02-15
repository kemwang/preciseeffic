package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.CodeAnalysisMethodDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.request.AffectMethodReq;
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
public interface CodeAnalysisMethodDetailMapper extends BaseMapper<CodeAnalysisMethodDetail> {
    int replaceIntoMethod(CodeAnalysisMethodDetail cas);

    List<CodeAnalysisMethodDetail> searchMethods(AffectMethodReq methodReq);

}
