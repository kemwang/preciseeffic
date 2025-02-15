package com.ruqimobility.precisiontest.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruqimobility.precisiontest.entity.persistent.JacgClassAnnotation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.persistent.MethodAnnotation;

import java.util.List;

/**
 * <p>
 * 类上的注解信息表 Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-12-05
 */
@DS(value = "callGraph")
public interface JacgClassAnnotationMapper extends BaseMapper<JacgClassAnnotation> {
    List<JacgClassAnnotation> getClassAnnotations(String tableName, String fullClassName);
}
