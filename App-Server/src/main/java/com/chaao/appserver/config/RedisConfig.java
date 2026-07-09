package com.chaao.appserver.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Spring Boot 3.x 适配版 Redis 配置类
 * 解决 setObjectMapper 弃用问题 + 中文乱码 + 日期序列化
 * 序列化，就是把「Java 对象 / 复杂数据」变成「字节数组 / 字符串」的过程，方便存到 Redis、文件或通过网络传输。
 * 反序列化就是反过来：把「字节数组 / 字符串」变回「Java 对象」。
 */
@Slf4j
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        log.info("开始初始化创建redis模板 RedisTemplate ...");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 1. 配置 KEY 序列化器（统一用字符串，避免乱码）
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // 2. 配置 VALUE 序列化器（新版构造器方式，替代弃用的 setObjectMapper）
        // 创建 ObjectMapper 并配置
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决 Java 8 日期类型序列化问题
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 新版方式：直接在构造 Jackson2JsonRedisSerializer 时传入 ObjectMapper
        Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);

        // 3. 初始化配置
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}