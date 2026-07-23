package com.chaao.appserver.websocket.admin;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 会话管理工具类
 * 功能：
 * 1. 存储在线管理员用户ID与对应会话Session
 * 2. 实现单点/全员推送消息
 * 3. 线程安全ConcurrentHashMap保证高并发稳定
 */
@Slf4j
@Component
public class WebSocketSessionManager {

    /**
     * 在线用户会话池
     * key: 登录管理员用户ID
     * value: 当前客户端WebSocket会话Session
     */
    private static final Map<Long, Session> ONLINE_ADMIN_SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 存入在线会话
     * @param adminId 管理员ID
     * @param session 客户端会话
     */
    public static void putSession(Long adminId, Session session) {
        ONLINE_ADMIN_SESSION_MAP.put(adminId, session);
        log.info("管理员【{}】建立订单推送连接，当前在线管理员数量：{}", adminId, ONLINE_ADMIN_SESSION_MAP.size());
    }

    /**
     * 移除离线会话
     * @param adminId 管理员ID
     */
    public static void removeSession(Long adminId) {
        ONLINE_ADMIN_SESSION_MAP.remove(adminId);
        log.info("管理员【{}】断开订单推送连接，当前在线管理员数量：{}", adminId, ONLINE_ADMIN_SESSION_MAP.size());
    }

    /**
     * 根据用户ID获取会话
     */
    public static Session getSessionByAdminId(Long adminId) {
        return ONLINE_ADMIN_SESSION_MAP.get(adminId);
    }

    /**
     * 推送消息给指定单个管理员
     * @param adminId 管理员ID
     * @param message 推送JSON消息
     */
    public static void sendMsgToOne(Long adminId, String message) {
        Session session = getSessionByAdminId(adminId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("向管理员【{}】推送消息失败", adminId, e);
            }
        }
    }

    /**
     * 全员推送（所有在线管理员都收到）
     * @param message 推送JSON字符串
     */
    public static void sendMsgToAll(String message) {
        ONLINE_ADMIN_SESSION_MAP.forEach((adminId, session) -> {
            if (session != null && session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    log.error("全员推送消息异常", e);
                }
            }
        });
    }
}