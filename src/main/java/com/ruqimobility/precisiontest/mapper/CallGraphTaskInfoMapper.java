package com.ruqimobility.precisiontest.mapper;

import com.ruqimobility.precisiontest.entity.persistent.CallGraphTaskInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 存储任务中，各项目各模块的调用链任务详情 Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-06-15
 */
public interface CallGraphTaskInfoMapper extends BaseMapper<CallGraphTaskInfo> {
    int replaceIntoCallGraphTaskInfo(CallGraphTaskInfo callGraphTaskInfo);
}
