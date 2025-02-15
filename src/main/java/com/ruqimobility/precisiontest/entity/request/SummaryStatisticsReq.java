package com.ruqimobility.precisiontest.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * 任务统计总览请求对象
 * @author liujia
 * @date 5/9 16:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryStatisticsReq {
    //任务id
    private Long taskId;
    //当searchType不传值时，默认查询所有，传递则查询具体服务。
    private Long taskInfoId;

}
