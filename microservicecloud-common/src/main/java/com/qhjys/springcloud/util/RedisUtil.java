package com.qhjys.springcloud.util;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // Base (int long float double char string boolean ...)
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // Object (list set map ...)
    public void setObject(String key, Object value) {
        set(key, JSON.toJSONString(value));
    }

    public void setObject(String key, Object value, long timeout, TimeUnit unit) {
        set(key, JSON.toJSONString(value), timeout, unit);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return JSON.parseObject(get(key), clazz);
    }

    // List
    //public void setList(String key, Object value) {
    //    set(key, JSON.toJSONString(value));
    //}

    //public void setList(String key, Object value, long timeout, TimeUnit unit) {
    //    set(key, JSON.toJSONString(value), timeout, unit);
    //}

    public <T> List<T> getList(String key, Class<T> clazz) {
        return JSON.parseArray(get(key), clazz);
    }

    // Set
    //public void setSet(String key, Object value) {
    //    set(key, JSON.toJSONString(value));
    //}

    //public void setSet(String key, Object value, long timeout, TimeUnit unit) {
    //    set(key, JSON.toJSONString(value), timeout, unit);
    //}

    public <T> Set<T> getSet(String key, Class<T> clazz) {
        return new HashSet<>(JSON.parseArray(get(key), clazz));
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public Boolean exist(String key) {
        return stringRedisTemplate.hasKey(key);
    }
}
