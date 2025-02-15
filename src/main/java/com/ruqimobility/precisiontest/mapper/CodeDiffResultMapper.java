package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.CodeDiffResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liujia
 * @since 2023-03-31
 */
@Repository
public interface CodeDiffResultMapper extends BaseMapper<CodeDiffResult> {
    int resultReplace(CodeDiffResult cdr);
}
