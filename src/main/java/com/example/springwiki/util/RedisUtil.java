package com.example.springwiki.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author jessica~
 * @version 1.0
 */
@Component
public class RedisUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);
    @Resource
    private RedisTemplate redisTemplate;

    public boolean validateRepeat(String key, long second) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            LOG.info("key已经存在:{}", key);
            return false;
        } else {
            LOG.info("key不存在,放入:{},过期{}秒", key, second);
            redisTemplate.opsForValue().set(key, key, second, TimeUnit.SECONDS);
            return true;
        }
    }
}
