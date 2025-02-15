package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

@Data
public class CallGraphNodeDto {

    private String branch;

    private String callClassName;

    private String callFullClassName;

    private String callMethodName;
}
