package com.taild.expresstix.infrastructure.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;

public interface RedisInfraService {

    void setString(String key, String value);
    String getString(String key);

    void setObject(String key, Object value);
    <T> T getObject(String key, Class<T> targetClass);
    boolean delete(String key);
    RedisTemplate<String, Object> getRedisTemplate();
}
