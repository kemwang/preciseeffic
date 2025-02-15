package com.ruqimobility.precisiontest.entity.response.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruqimobility.precisiontest.constants.enums.StatusCode;
import lombok.Data;

/**
 * controller层统一响应体
 *
 * @author liujia
 * @date 2023/9/22
 */
@Data
public class OpenApiResponse<T> {
    private Integer code;
    private String msg;
    @JsonProperty("content")
    private T data;

    public OpenApiResponse() {
        this.code = StatusCode.SERVICE_RUN_SUCCESS.getStatus();
    }

    public static <T> OpenApiResponse<T> build(int status, String msg) {
        return build(status, msg, null);
    }

    public static <T> OpenApiResponse<T> build(Status status, String msg) {
        return build(status.getStatus(), msg, null);
    }

    public static <T> OpenApiResponse<T> build(Status status) {
        return build(status.getStatus(), status.getMsg(), null);
    }

    public static <T> OpenApiResponse<T> build(int status, String msg, T data) {
        OpenApiResponse<T> response = new OpenApiResponse<>();
        response.setCode(status);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    public static <T> OpenApiResponse<T> success() {
        return build(StatusCode.SERVICE_RUN_SUCCESS);
    }

    public static <T> OpenApiResponse<T> success(T data) {
        return build(StatusCode.SERVICE_RUN_SUCCESS.getStatus(), StatusCode.SERVICE_RUN_SUCCESS.getMsg(), data);
    }
}
