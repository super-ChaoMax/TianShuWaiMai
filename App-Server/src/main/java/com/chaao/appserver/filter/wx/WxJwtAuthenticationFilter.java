package com.chaao.appserver.filter.wx;

import com.chaao.appserver.service.SpringSecurity.wx.WxUserDetails;
import com.chaao.appserver.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.rbac.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


//微信提供了专属的微信登录专用过滤器
public class WxJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WxJwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        // 微信登录接口地址
        setFilterProcessesUrl("/wx/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // 前端传 username 存 openid，password 随便传空/固定值
            LoginRequest loginDto = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(), // 此处是微信openid
                            loginDto.getPassword()
                    );
            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException("读取微信登录请求失败", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 核心修改：强转微信用户详情，不是后台
        WxUserDetails wxUserDetails = (WxUserDetails) authResult.getPrincipal();

        // 生成微信用户token，type固定2
        String token = jwtUtil.generateLoginToken(
                wxUserDetails.getUsername(), // openid
                wxUserDetails.getWxUserId(),
                2  // 微信用户类型
        );

        request.setAttribute("token", token);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}