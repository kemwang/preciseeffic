package com.ruqimobility.precisiontest.entity.response.controller;

import lombok.Data;

import java.util.List;
@Data
public class CodeDiffTaskRespWrapper {

    private Long total;

    private Long size;

    private Long pages;

    private List<CodeDiffTaskResp> records;


}
