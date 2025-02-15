package com.ruqimobility.precisiontest.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

/**
 * 用于临时同步数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@TableName
@ToString
public class OdsStory {
    private BigInteger id;

    @ApiModelProperty(value = "项目id")
    private String workspaceId;

    @ApiModelProperty(value = " tapd 是否需要测试")
    private String needTest;

    @ApiModelProperty(value = "发布计划ID")
    private String releaseId;

    @ApiModelProperty(value = "tapd需求创建时间")
    private String created;

    @ApiModelProperty(value = "tapd需求最后更新时间")
    private String modified;

    @ApiModelProperty(value = "上线时间")
    private String onlineTime;

    @ApiModelProperty(value = "上线周数")
    private String onlineYearWeek;

    @ApiModelProperty(value = "上线季度数")
    private String onlineYearQuarter;

    @ApiModelProperty(value = "状态")
    private String status;
}
