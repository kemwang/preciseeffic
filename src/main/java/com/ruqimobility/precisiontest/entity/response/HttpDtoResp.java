package com.ruqimobility.precisiontest.entity.response;

import com.ruqimobility.precisiontest.entity.persistent.FindParam;
import lombok.Data;

/**
 * @author lj
 * @title HttpDtoResp
 * @description
 * @create 2023/12/12 15:10
 **/
@Data
public class HttpDtoResp {
    private Long pid;
    private String target;
    private String commitId;
    private String httpMethod;
    private String requestDto;
    private FindParam findParam;
    private String aiAnswer;
}
