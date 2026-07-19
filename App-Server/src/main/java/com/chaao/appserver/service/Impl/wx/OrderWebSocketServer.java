package com.chaao.appserver.service.Impl.wx;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/order/{orderId}")
public class OrderWebSocketServer {
    // key:订单号 value:当前小程序会话
    public static final Map<String, Session> orderSessionMap = new ConcurrentHashMap<>();

    // ========== 【原有】存放订单最后活跃时间，用于心跳超时判断 ==========
    public static final Map<String, Long> activeTimeMap = new ConcurrentHashMap<>();

    // ========== 【原有】心跳超时阈值：5秒无心跳判定用户离开页面 ==========
    public static final long HEART_TIMEOUT = 5 * 1000;

    //【新增】存放每个订单独立心跳超时时间 未扫码默认20秒
    public static final Map<String,Long> orderTimeoutMap = new ConcurrentHashMap<>();

    // 小程序建立连接时触发，携带订单id
    @OnOpen
    public void onOpen(@PathParam("orderId") String orderId, Session session) {
        orderSessionMap.put(orderId, session);
        // ========== 【原有】建立连接时初始化当前订单活跃时间 ==========
        activeTimeMap.put(orderId, System.currentTimeMillis());
        //【新增】新订单默认设置20秒心跳超时
        orderTimeoutMap.putIfAbsent(orderId,20*1000L);
        System.out.println("订单" + orderId + "小程序已连接");
    }

    // 向指定订单的小程序推送消息
    public static void sendMsg(String orderId, String msg) {
        Session session = orderSessionMap.get(orderId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ========== 【原有】外部调用刷新订单活跃时间（前端心跳接口调用） ==========
    public static void refreshActive(String orderId){
        activeTimeMap.put(orderId, System.currentTimeMillis());
    }

    //【新增】对外提供修改订单心跳超时时间方法
    public static void setHeartTimeout(String orderId,long time){
        orderTimeoutMap.put(orderId,time);
    }

    // 小程序关闭页面断开连接
    @OnClose
    public void onClose(@PathParam("orderId") String orderId) {
        orderSessionMap.remove(orderId);
        // ========== 【原有】断开连接移除活跃记录，并推送取消支付消息 ==========
        activeTimeMap.remove(orderId);
        // 直接把activeTimeMap里这条订单删掉了
        //👉 定时任务遍历直接找不到这条订单，自然永远不会执行超时判断！

        //取消 WS 断开自动删超时数据，只靠心跳定时判超时

        //【新增】移除订单超时配置
//        orderTimeoutMap.remove(orderId);
//        String closeJson = "{\"type\":\"close\",\"msg\":\"小程序端断开连接，取消支付\"}";
//        sendMsg(orderId,closeJson);
    }

    @OnMessage
    public void onMessage(String message) {}
}