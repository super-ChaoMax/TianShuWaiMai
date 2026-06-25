package com.chaao.appserver.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.Map;


/**
 * JWT 工具类
 * 用于生成、解析和验证 JWT Token
 *
 * JWT 结构说明：
 * - Header（头部）：指定签名算法（HS256）
 * - Payload（载荷）：包含用户信息和自定义数据
 * - Signature（签名）：使用密钥对 Header+Payload 进行签名，防止篡改
 */
@Component
public class JwtUtil {

    private final String secretKey;
    private final long expireTime;


    /**
     * 构造函数注入配置参数
     * 
     * @param secretKey JWT 签名密钥，从配置文件读取，至少需要32个字符（256位）
     *                  对应配置项：jwt.secret
     * @param expireTime Token 过期时间（秒），从配置文件读取后转换为毫秒
     *                   对应配置项：jwt.expire，默认86400秒（24小时）
     */
    public JwtUtil(@Value("${jwt.secret:jwt_secret_key_ZZCJWT123456}") String secretKey,
                   @Value("${jwt.expire:86400}") long expireTime) {
        if (secretKey == null || secretKey.getBytes().length < 32) {
            throw new IllegalArgumentException("JWT密钥长度至少需要32个字符");
        }
        this.secretKey = secretKey;
        this.expireTime = expireTime * 1000;
    }



    /**
     * 获取签名密钥
     * 使用 HMAC-SHA256 算法生成安全的签名密钥
     * 
     * @return Key 对象，用于签名和验证
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }



    /**
     * 生成简单的 JWT Token
     * 适用于只需要存储用户名的场景
     * 
     * @param username 用户名，作为 Token 的 subject（主体）
     * @return 生成的 JWT Token 字符串
     * 
     * Token 包含的信息：
     * - sub（主题）：用户名
     * - iat（签发时间）：当前时间戳
     * - exp（过期时间）：当前时间 + 配置的过期时长
     */
    public String generateToken(String username) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expireTime;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * 解析并验证 JWT Token
     * 
     * @param token 待验证的 JWT Token 字符串
     * @return Claims 对象，包含 Token 中的所有载荷信息
     * @throws RuntimeException 当 Token 过期或无效时抛出异常
     * 
     * 验证流程：
     * 1. 验证签名是否正确（防止篡改）
     * 2. 验证 Token 是否过期
     * 3. 解析出载荷内容
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token 已过期");
        } catch (JwtException e) {
            throw new RuntimeException("无效的 Token");
        }
    }



    /**
     * 生成带自定义载荷的 JWT Token
     * 适用于需要存储额外用户信息的场景（如用户ID、角色等）
     * 
     * @param subject Token 主体（通常是用户名）
     * @param claims 自定义载荷 Map，例如：{"id": 1, "role": "admin"}
     * @return 生成的 JWT Token 字符串
     * 
     * 使用示例：
     * <pre>
     * Map<String, Object> claims = new HashMap<>();
     * claims.put("id", user.getId());
     * claims.put("role", user.getRole());
     * String token = jwtUtil.generateToken(user.getUsername(), claims);
     * </pre>
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        Date expDate = new Date(now + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }




}

