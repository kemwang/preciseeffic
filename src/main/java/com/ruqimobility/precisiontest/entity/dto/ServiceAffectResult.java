package com.ruqimobility.precisiontest.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author lj
 * @title ServiceAffectResult
 * @description
 * @create 2024/1/17 15:59
 **/

@Data
public class ServiceAffectResult {
    @JsonProperty("project_name")
    private String currentTapdName;
    @JsonProperty("task_name")
    private String taskName;
    @JsonProperty("story_info")
    private String storyInfoText;
    @JsonProperty("task_url")
    private String taskUrl;
    @JsonProperty("affect_count")
    private int affectCount;
    @JsonProperty("change_count")
    private int changeCount;
    @JsonProperty("path_count")
    private int changeHttp;
    @JsonProperty("users")
    private String noticeUsers;

    private Set<String> noticeProject;
    @JsonProperty("group_table")
    private List<HttpUrl> groupTable;
    @Data
    public static class HttpUrl{
        @JsonProperty("affect_service")
        public String affectService;
        @JsonProperty("change_service")
        public String changeService;
        @JsonProperty("path")
        public String httpPath;
    }
}
