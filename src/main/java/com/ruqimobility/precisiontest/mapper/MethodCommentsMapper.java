package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.MethodComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-04-26
 */
public interface MethodCommentsMapper extends BaseMapper<MethodComments> {
    int upsert(MethodComments methodComments);
}
