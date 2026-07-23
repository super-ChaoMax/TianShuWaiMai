package com.chaao.appserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 全局配置类
 * 作用：自动扫描 @ServerEndpoint 注解的 WebSocket 端点并注册
 * 启动 WebSocket 服务支持
 */
@Configuration
public class WebSocketConfig {


    /**
     * 注入端点扫描器
     * 项目启动时自动加载所有使用 @ServerEndpoint 的 WebSocket 服务
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}