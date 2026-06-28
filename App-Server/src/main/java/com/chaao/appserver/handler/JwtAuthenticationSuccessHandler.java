package com.chaao.appserver.handler; // 请根据你的包结构修改

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 设置响应类型
        response.setContentType("application/json;charset=UTF-8");

        // 获取用户名
        String username = authentication.getName();

        // TODO: 这里应该调用 JwtUtil 生成 Token
        // 注意：因为 Handler 通常由 Spring 管理，建议在这里注入 JwtUtil，
        // 或者像你现在这样在 Filter 里生成好放在 Principal 里传过来。
        // 为了演示简单，假设这里能拿到 token 字符串
        String token = "Bearer " + request.getAttribute("token"); // 示例获取方式

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "登录成功");
        result.put("data", token);

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}