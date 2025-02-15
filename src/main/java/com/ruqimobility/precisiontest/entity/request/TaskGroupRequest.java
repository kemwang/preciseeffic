package com.ruqimobility.precisiontest.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruqimobility.precisiontest.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
public class TaskGroupRequest extends PageRequest {
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "祺效提测单code")
    private String orderCode;

    @ApiModelProperty(value = "项目ID")
    private String tapdProjectId;

    @ApiModelProperty(value = "TAPD 需求ID")
    private String storyId;
}
