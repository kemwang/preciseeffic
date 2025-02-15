package com.ruqimobility.precisiontest.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruqimobility.precisiontest.entity.persistent.StoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 文件夹 Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-10-26
 */
@DS("casePlat")
public interface StoryInfoMapper extends BaseMapper<StoryInfo> {

}
