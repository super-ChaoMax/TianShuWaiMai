package com.chaao.appserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

// 必须加这个注解！启动Spring上下文才能注入Bean
@SpringBootTest
public class RedisTest {
    // 注入自定义序列化好的RedisTemplate
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 测试普通字符串存取
    @Test
    void testStringRedis(){
        // 存数据
        redisTemplate.opsForValue().set("test:name","Redis零基础学习");
        // 存数据+设置过期时间 60秒
        redisTemplate.opsForValue().set("test:code","666888",60, TimeUnit.SECONDS);

        // 取数据
        String name = (String) redisTemplate.opsForValue().get("test:name");
        String code = (String) redisTemplate.opsForValue().get("test:code");

        System.out.println("获取name值：" + name);
        System.out.println("获取验证码：" + code);

    }

    // 测试存User对象（自动序列化）
    @Test
    void testSaveUserObj(){
//        User user = new User(1L,"小明",19,"123456");
        // 直接存入对象，自动JSON序列化
//        redisTemplate.opsForValue().set("user:1001",user);

        // 取出自动反序列化
//        User userInfo = (User) redisTemplate.opsForValue().get("user:1001");
//        System.out.println("取出用户信息："+userInfo);
    }

    // 删除key测试
    @Test
    void testDelKey(){
        Boolean delete = redisTemplate.delete("test:name");
        System.out.println("是否删除成功："+delete);
    }



}
