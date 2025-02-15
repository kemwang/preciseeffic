package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.MethodComments;
import com.ruqimobility.precisiontest.entity.persistent.QxJarInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-04-28
 */
public interface QxJarInfoMapper extends BaseMapper<QxJarInfo> {
    int upsert(QxJarInfo qxJarInfo);
}
