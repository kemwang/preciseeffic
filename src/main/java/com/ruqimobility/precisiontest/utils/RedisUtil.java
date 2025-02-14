package com.ruqimobility.precisiontest.utils;



import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类.</br>
 * (基于RedisTemplate)
 *
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<Object,Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置缓存，失效时间为7天
     * */
    public void saveObjectList(String key, List<Object> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    /**
     * 获取缓存信息
     * */
    public List<Object> getObjectList(String key) {
        if(!redisTemplate.hasKey(key)){
            return null;
        }
        System.out.println("redis:"+redisTemplate.opsForList().range(key, 0, -1));
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 读取缓存
     */
    public String get(final String key) {
        boolean hasKey = Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
        if(hasKey) {
            return stringRedisTemplate.opsForValue().get(key);
        }else{
            return null;
        }
    }

    public List<String> getAll(final String prefix) {
        // 获取所有的key
        Set<String> keys = stringRedisTemplate.keys(prefix);
        // 批量获取数据
        if (keys != null) {
            return stringRedisTemplate.opsForValue().multiGet(keys);
        }
        return null;
    }

    /**
     * 写入缓存
     */
    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存,包含过期时间
     */
    public boolean setex(final String key, String value, Integer i) {
        boolean result = false;
        try {
            stringRedisTemplate.opsForValue().set(key, value,i, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 更新缓存
     */
    public boolean getAndSet(final String key, String obj, Integer i) {
        boolean result = false;
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            try {
                stringRedisTemplate.opsForValue().getAndSet(key, obj);
                stringRedisTemplate.expire(key, i, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            this.setex(key,obj,i);
        }
        result = true;
        return result;
    }

    /**
     * 删除缓存
     */
    public boolean delete(final String key) {
        boolean result = false;
        try {
            stringRedisTemplate.delete(key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}