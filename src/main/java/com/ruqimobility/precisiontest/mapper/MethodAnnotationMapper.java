package com.ruqimobility.precisiontest.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruqimobility.precisiontest.entity.persistent.MethodAnnotation;

import java.util.List;

/**
 * <p>
 * 方法上的注解信息表 Mapper 接口
 * </p>
 *
 * @author lvzhiqi
 * @since 2023-05-26
 */

@DS(value = "callGraph")
public interface MethodAnnotationMapper extends BaseMapper<MethodAnnotation> {
    List<MethodAnnotation> getMethodAnnotation(String tableName, String fullMethod);

    List<MethodAnnotation> getMethodAnnotationlikely(String tableName, String method);

    List<MethodAnnotation> getApiUri(String tableName,String fullMethod);
}
