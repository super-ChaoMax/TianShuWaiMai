package com.chaao.appserver.config;

import com.chaao.appserver.filter.JwtAuthenticationFilter;
import com.chaao.appserver.service.SpringSecurity.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter; // 注意：这里不需要手动注入 CorsFilter Bean，Spring Security 内部会处理
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.List; // 【新增】引入 List 用于更稳健的配置

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 【重点修改区域 1】全局跨域配置
     * 原因分析：之前的 setAllowedOriginPatterns("*") 配合 allowCredentials(true) 容易导致部分环境下的 CORS 预检失败。
     * 修改建议：明确指定允许的源，或者使用 addAllowedOriginPattern 确保语法正确。
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 【修改点 1.1】：为了调试方便，暂时允许所有来源。
        // 生产环境建议改为具体的域名，如 "https://your-frontend.com"
        // 使用 "*" 时，必须确保使用的是 addAllowedOriginPattern 而不是 setAllowedOrigins
        config.addAllowedOriginPattern("*");

        // 【修改点 1.2】：允许携带凭证（Cookie/Token），这对 JWT 很重要
        config.setAllowCredentials(true);

        // 【修改点 1.3】：明确允许的方法。虽然 "*" 也可以，但显式声明更稳妥
        // 特别是 OPTIONS 方法，它是跨域预检必须的
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 【修改点 1.4】：允许所有头信息，包括 Authorization
        config.addAllowedHeader("*");

        // 【新增】暴露 Authorization 头给前端（可选，视前端需求而定）
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // 【重点修改区域 2】开启跨域配置
                // 确保这里引用了上面修改过的 corsConfigurationSource
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailService)
                .authorizeHttpRequests(auth -> auth
                        // 【重点修改区域 3】放行路径配置
                        .requestMatchers(
                                "/admin/login",
                                "/wx/login",
                                "/index.html",
                                "/ws/order/**",
//                                "/wx/update/profile",
//                                "/wx/get/phone",

                                // 【必须添加】：根据报错日志，你的前端正在请求这个接口
                                // 如果不放开，且用户未登录（或 Token 无效），就会报 403
//                                "/wx/dish/list",      // 精确匹配当前报错接口
//                                "/wx/dish/**",        // 放行所有菜品相关接口（推荐）
//                                "/wx/shoppingCart/**",// 放行购物车接口（对应你之前的另一个报错）
//                                "/wx/category/**",    // 放行分类接口
//                                "/admin/employee/**",
//                                "/wx/pay/**",               //支付接口测试

                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )
                // JWT过滤器放在账号密码过滤器前面
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}