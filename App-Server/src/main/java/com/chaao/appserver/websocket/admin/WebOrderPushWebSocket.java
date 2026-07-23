package com.chaao.appserver.websocket.admin;

import com.alibaba.fastjson2.JSON;
import com.chaao.appserver.util.JwtUtil;
import com.chaao.appserver.util.StaticBeanContext;
import entity.admin.WsOrderMsg;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 订单消息推送 WebSocket 服务端点
 * 访问地址格式：ws://localhost:端口/ws/order?token=登录令牌
 * 功能：
 * 1. 连接建立时通过Token解析登录管理员信息
 * 2. 监听连接开启、关闭、异常、客户端消息
 * 3. 统一管理会话上下线
 */
@Slf4j
@Component
@ServerEndpoint("/ws/admin/order")
public class WebOrderPushWebSocket {

    //@ServerEndpoint 注解的类，Spring 默认不管理它的生命周期，
    // 所以 @Autowired 注入的 JwtUtil 永远是 null，直接调用方法就会报空指针异常。
//    @Autowired
//    private JwtUtil jwtUtil;


    /**
     * 连接建立成功触发
     * @param session 当前客户端会话
     * @param config 连接请求配置
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        try {
            // 1. 获取请求参数（正确类型）
            Map<String, List<String>> paramMap = session.getRequestParameterMap();

            // 2. 获取 token（取参数列表的第一个值）
            List<String> tokenList = paramMap.get("token");
            if (tokenList == null || tokenList.isEmpty()) {
                log.warn("WebSocket连接失败：未携带登录Token");
                session.close();
                return;
            }
            String token = tokenList.get(0);

            if (!StringUtils.hasText(token)) {
                log.warn("WebSocket连接失败：Token为空");
                session.close();
                return;
            }

            // 3. 后续的Token解析逻辑不变
            Long adminId = parseTokenGetAdminId(token);
            if (adminId == null) {
                log.warn("WebSocket连接失败：Token无效或已过期");
                session.close();
                return;
            }

            WebSocketSessionManager.putSession(adminId, session);

        } catch (Exception e) {
            log.error("建立订单WebSocket连接异常", e);
        }
    }

    /**
     * 客户端关闭连接触发
     */
    @OnClose
    public void onClose(Session session) {
        try {
            // 1. 正确获取参数Map（返回 List<String>）
            Map<String, List<String>> paramMap = session.getRequestParameterMap();
            List<String> tokenList = paramMap.get("token");

            if (tokenList != null && !tokenList.isEmpty()) {
                // 2. 取第一个 token 值
                String token = tokenList.get(0);
                Long adminId = parseTokenGetAdminId(token);
                if (adminId != null) {
                    WebSocketSessionManager.removeSession(adminId);
                }
            }
        } catch (Exception e) {
            log.error("关闭WebSocket连接异常", e);
        }
    }

    /**
     * 客户端发送消息触发（后台订单推送基本用不到，预留）
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到客户端消息：{}", message);
    }

    /**
     * 连接异常触发
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("订单WebSocket连接出现异常", error);
    }

    /**
     * 【工具方法】解析Token获取管理员ID
     * 替换成你项目中已有的 JwtUtil.getUserByToken(token)
     */
    /**
     * 解析Token获取当前登录管理员ID
     * @param token 前端传入登录令牌
     * @return 管理员ID，解析失败返回null
     */
    private Long parseTokenGetAdminId(String token) {
        try {
            JwtUtil jwtUtil = StaticBeanContext.getBean(JwtUtil.class);
            return jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            log.error("解析Token失败", e);
            return null;
        }
    }

    // ====================== 对外暴露静态推送方法（业务层直接调用）======================

    /**
     * 推送新订单提醒给所有在线管理员
     * @param orderCount 新订单数量
     * @param orderNo 最新订单号
     */
    public static void pushNewOrderNotice(Integer orderCount, String orderNo) {
        // 组装统一推送实体
        WsOrderMsg wsMsg = new WsOrderMsg();
        wsMsg.setType("NEW_ORDER");
        wsMsg.setOrderCount(orderCount);
        wsMsg.setOrderNo(orderNo);
        wsMsg.setMsg("您有新的外卖订单，请及时处理");
        // 对象转JSON推送前端
        String jsonStr = JSON.toJSONString(wsMsg);
        // 全员推送
        WebSocketSessionManager.sendMsgToAll(jsonStr);
    }
}