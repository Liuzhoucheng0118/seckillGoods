package com.lzc.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/5 21:24
 * @Version 1.0
 */
@Configuration
public class RedisConfigration {

    /**
     * redis序列化配置
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//        key
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        value
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        hash key
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        hash value
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
