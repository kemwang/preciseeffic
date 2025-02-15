package com.ruqimobility.precisiontest.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.persistent.JacgMethodInfo;

import java.util.List;

/**
 * <p>
 * 方法的信息表 Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-06-08
 */
@DS("callGraph")
public interface JacgMethodInfoMapper extends BaseMapper<JacgMethodInfo> {
    List<String> fuzzySelectCalleeFullMethods(String tableName, String className);
}
