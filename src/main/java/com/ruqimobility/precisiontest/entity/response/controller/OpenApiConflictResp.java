package com.ruqimobility.precisiontest.entity.response.controller;

import com.ruqimobility.precisiontest.entity.dto.OpenApiConflictDto;
import lombok.Data;

import java.util.List;

/**
 * @author lj
 * @title OpenApiConflictDto
 * @description
 * @create 2023/10/12 16:20
 **/
@Data
public class OpenApiConflictResp {
    private List<OpenApiConflictDto> orders;
}
