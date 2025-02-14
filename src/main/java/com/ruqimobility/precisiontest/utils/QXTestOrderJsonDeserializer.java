package com.ruqimobility.precisiontest.utils;

import com.alibaba.fastjson.JSON;
import com.ruqimobility.precisiontest.entity.response.QXTestOrderResp;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class QXTestOrderJsonDeserializer implements Deserializer<QXTestOrderResp> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public QXTestOrderResp deserialize(String s, byte[] bytes) {
        return JSON.parseObject(bytes, QXTestOrderResp.class);
    }

    @Override
    public void close() {

    }
}
