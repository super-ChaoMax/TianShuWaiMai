package com.chaao.appserver.filter;

import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.service.SpringSecurity.UserDetailServiceImpl;
import com.chaao.appserver.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j; // 引入 Slf4j 日志，方便排查问题
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 * 继承 OncePerRequestFilter：Spring Security 提供的基类，保证每个请求只执行一次过滤逻辑
 */
@Slf4j
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


        // 【新增】如果是 OPTIONS 预检请求，直接放行，不要拦截
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }


        // 1. 【修复1：对齐前端标准】从标准的 Authorization 头获取 Token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        // 校验 Token 是否存在，且必须以 "Bearer " 开头（注意 Bearer 后面有个空格）
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 截取掉前缀，提取纯 Token 字符串
        }

        // 2. 【白名单放行】如果请求头中没有 Token，直接放行
        // 说明：未登录用户的请求（如登录、注册、公开接口）不会携带 Token
        // 这里直接放行，后续由 Spring Security 的 SecurityConfig 决定该接口是否允许匿名访问
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 3. 【解析 JWT】调用工具类解析 Token，如果 Token 过期或签名被篡改，会直接抛出异常
            Claims claims = jwtUtil.parseToken(token);

            // 4. 【修复2：防御性编程】安全获取 userType，防止 JWT 中缺失该字段导致空指针异常
            Object userTypeObj = claims.get("userType");
            if (userTypeObj == null) {
                log.warn("JWT 中缺失 userType 字段，跳过认证");
                System.out.println("JWT 中缺失 userType 字段，跳过认证");
                filterChain.doFilter(request, response);
                return;
            }
            Integer userType = Integer.parseInt(userTypeObj.toString());

            // 5. 【修复3：并发安全】使用局部变量代替直接在 ThreadLocal 中操作
            // 说明：Tomcat 使用线程池，如果 ThreadLocal 清理不及时，会导致下一个请求“继承”上一个用户的身份
            LoginUser loginUser = null;
            if (1 == userType) {
                // 后台管理员：通过 username 加载用户信息
//                String username = claims.get("username").toString();
                String username = claims.getSubject(); // <-- 改成这行
                UserDetailServiceImpl.LOGIN_TYPE.set(1); // 仅在必要时设置 ThreadLocal
                loginUser = (LoginUser) userDetailService.loadUserByUsername(username);
            } else if (2 == userType) {

                System.out.println("微信小程序用户：通过 openid 加载用户信息");

                // 微信小程序用户：通过 openid 加载用户信息
                String openid = claims.get("openid").toString();
                UserDetailServiceImpl.LOGIN_TYPE.set(2);
                loginUser = (LoginUser) userDetailService.loadUserByUsername(openid);
                System.out.println("解析出来的微信用户ID：" + openid);
            }

            // 6. 【存入安全上下文】如果成功解析出用户，将其存入 Spring Security 上下文
            if (loginUser != null) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                loginUser,
                                null, // 凭证（密码），认证后通常置为 null
                                loginUser.getAuthorities() // 用户的权限集合
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            // 7. 【异常处理】Token 解析失败（如过期、非法）
            // 说明：这里不要直接返回 401，而是记录日志并放行。
            // 因为如果是公开接口，即使 Token 无效也应该允许访问；
            // 如果是受保护接口，Spring Security 后续发现 SecurityContext 为空，会自动抛出 401。
            log.warn("JWT Token 校验失败: {}", e.getMessage());
        } finally {
            // 8. 【修复4：彻底清理】无论 try 块中是否发生异常或提前 return，finally 都会执行
            // 必须清空 ThreadLocal，防止内存泄漏和并发环境下的身份污染
            UserDetailServiceImpl.LOGIN_TYPE.remove();
        }

        // 9. 【放行请求】将请求传递给过滤器链的下一个节点
        filterChain.doFilter(request, response);
    }
}