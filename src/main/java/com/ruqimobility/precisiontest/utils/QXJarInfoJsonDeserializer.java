package com.ruqimobility.precisiontest.utils;

import com.alibaba.fastjson.JSON;
import com.ruqimobility.precisiontest.entity.response.JarInfoMessageResp;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class QXJarInfoJsonDeserializer implements Deserializer<JarInfoMessageResp> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public JarInfoMessageResp deserialize(String s, byte[] bytes) {
        return JSON.parseObject(bytes, JarInfoMessageResp.class);
    }

    @Override
    public void close() {

    }
}
