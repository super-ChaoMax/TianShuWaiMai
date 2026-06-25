package com.chaao.appserver.filter.admin;



//修改了这两个配置，因为现在都是放在pojo模块了
import com.chaao.appserver.service.SpringSecurity.admin.SysUserDetails;
import dto.rbac.LoginRequest;
import com.chaao.appserver.util.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain; // 确保引入 Jakarta 包的 FilterChain
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// 【修正 1】：引入 Spring Security 的 Authentication 接口
import org.springframework.security.core.Authentication;
// 【修正 2】：引入 Spring Security 的认证异常
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class SysJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;


    private final JwtUtil jwtUtil; // 定义为 final，确保不可变

//    // 【关键】：必须注入 JwtUtil，否则 Filter 里也是 null
//    @Autowired
//    private JwtUtil jwtUtil;



    // 【优化】：将 ObjectMapper 提取为成员变量，避免频繁创建对象
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SysJwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        // 设置拦截路径 (注意：这里必须与你 SecurityConfig 中 permitAll 的路径一致)
        setFilterProcessesUrl("/admin/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // 从请求流中读取 JSON
            LoginRequest loginDto = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            // 封装 Token (此时未认证)
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    );

            // 交由 Manager 验证 (会调用 UserDetailsService 和 PasswordEncoder)
            return authenticationManager.authenticate(authRequest);

        } catch (IOException e) {
            // 将 IO 异常包装为运行时异常或直接抛出认证异常
            throw new RuntimeException("读取登录请求失败", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // ------- 强转为后台登录用户详细---------
        SysUserDetails  userDetails = (SysUserDetails) authResult.getPrincipal();


        // 【关键修改】：在这里生成 Token，并存入 request 属性，供 SuccessHandler 使用
        // 这样做的好处是 Filter 负责“认证”，Handler 负责“响应”，解耦了。
        String token = jwtUtil.generateLoginToken(
                userDetails.getUsername(),
                userDetails.getSysUser().getId(),
                1               // 用户类型 1后台 2微信
        );

        request.setAttribute("token", token);

        // 调用父类方法，这会自动触发我们在 Config 里设置的 setAuthenticationSuccessHandler
        super.successfulAuthentication(request, response, chain, authResult);


    }
}