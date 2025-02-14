package com.ruqimobility.precisiontest.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public final class JsonHelper {

    public static String toJsonString(Object object, boolean prettyFormat) {
        return JSON.toJSONString(object, prettyFormat);
    }

    public static String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T parseObject(String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz);
    }

    public static <T> List<T> parseArray(String str, Class<T> clazz) {
        return JSON.parseArray(str, clazz);
    }
}
