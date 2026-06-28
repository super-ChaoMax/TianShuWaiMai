package com.chaao.appserver.filter;

import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.service.SpringSecurity.UserDetailServiceImpl;
import com.chaao.appserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    try {
        // 1. 获取token（统一用 header: token）
        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims;
        try {
            // 解析JWT
            claims = jwtUtil.parseToken(token);
        } catch (Exception e) {
            // token过期/非法 直接放行，不认证
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 取出用户类型 1管理员 2微信用户
        Integer userType = Integer.parseInt(claims.get("userType").toString());
        LoginUser loginUser = null;

        if (1 == userType) {
            // 后台管理员：拿username
            String username = claims.get("username").toString();
            UserDetailServiceImpl.LOGIN_TYPE.set(1);
            loginUser = (LoginUser) userDetailService.loadUserByUsername(username);
        } else if (2 == userType) {
            // 微信小程序用户：拿openid
            String openid = claims.get("openid").toString();
            UserDetailServiceImpl.LOGIN_TYPE.set(2);
            loginUser = (LoginUser) userDetailService.loadUserByUsername(openid);
        }

        // 用完清除ThreadLocal
        UserDetailServiceImpl.LOGIN_TYPE.remove();

        // 3. 存入Security上下文
        if (loginUser != null) {
            org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken
                    = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    loginUser, null, loginUser.getAuthorities()
            );
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);

    } finally {
        // ========== 最终唯一清理位置 ==========
        // 整个请求链路结束，统一清空线程变量
        UserDetailServiceImpl.LOGIN_TYPE.remove();
        // 顺便清空Security上下文（好习惯）
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }


    }
}