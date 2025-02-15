package com.ruqimobility.precisiontest.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lj
 * @title serviceAffectDto
 * @description
 * @create 2024/1/16 13:59
 **/
@Data
public class ServiceAffectDto {
    public String currentService;
    public String gitlabName;
    public String gitlabId;
    public String http_url;
    public String http_desc;
}
