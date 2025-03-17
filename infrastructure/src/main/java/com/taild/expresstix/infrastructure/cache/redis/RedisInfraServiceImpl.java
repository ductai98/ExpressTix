package com.taild.expresstix.infrastructure.cache.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class RedisInfraServiceImpl implements RedisInfraService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void setString(String key, String value) {
        if (StringUtils.hasLength(key)) {
            return;
        }

        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key)
        ).map(String::valueOf).orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        if (!StringUtils.hasLength(key)) {
            return;
        }

        try {
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            log.error("setObject error:{}",e.getMessage());
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        //log.info("get Cache::{}", result);
        if (result == null) {
            return null;
        }

        // if result is a Map, convert it to the target class
        if (result instanceof Map) {
            try {
                return objectMapper.convertValue(result, targetClass);
            } catch (IllegalArgumentException e) {
                log.error("Error converting Map to object: {}", e.getMessage());
                return null;
            }
        }

        // if result is a String, convert it to the target class
        if (result instanceof String) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue((String) result, targetClass);
            } catch (JsonProcessingException e) {
                log.error("Error deserializing JSON to object: {}", e.getMessage());
                return null;
            }
        }

        // if result is neither a Map nor a String, return null
        return null;
    }

    @Override
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
