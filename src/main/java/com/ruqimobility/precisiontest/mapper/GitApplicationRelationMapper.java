package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.CodeDiffResult;
import com.ruqimobility.precisiontest.entity.persistent.GitApplicationRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lj
 * @since 2024-01-03
 */
@Repository
public interface GitApplicationRelationMapper extends BaseMapper<GitApplicationRelation> {
    int insertOrUpdate(GitApplicationRelation gitApplicationRelation);
}
