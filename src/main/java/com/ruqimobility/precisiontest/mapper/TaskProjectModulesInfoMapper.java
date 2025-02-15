package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.TaskProjectModulesInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-05-11
 */
public interface TaskProjectModulesInfoMapper extends BaseMapper<TaskProjectModulesInfo> {
    int  replaceIntoTaskProjectModulesInfo(TaskProjectModulesInfo taskProjectModulesInfo);
}
