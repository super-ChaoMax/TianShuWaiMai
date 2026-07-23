package com.chaao.appserver.service.Impl.wx;

import com.alibaba.fastjson2.JSON;
import com.chaao.appserver.websocket.pay.ChatWebSocket;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import vo.PaySend;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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

    // 新增：活跃连接计数器
    public static final AtomicInteger activeConnectionCount = new AtomicInteger(0);


    // 小程序建立连接时触发，携带订单id
    @OnOpen
    public void onOpen(@PathParam("orderId") String orderId, Session session) {
        orderSessionMap.put(orderId, session);
        activeTimeMap.put(orderId, System.currentTimeMillis());
        orderTimeoutMap.putIfAbsent(orderId,20*1000L);
        activeConnectionCount.incrementAndGet();
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
// 小程序关闭页面断开连接
// 小程序关闭页面断开连接
    @OnClose
    public void onClose(@PathParam("orderId") String orderId) {
        orderSessionMap.remove(orderId);
        activeTimeMap.remove(orderId);
        orderTimeoutMap.remove(orderId);
        // 唯一合法减计数入口
        activeConnectionCount.decrementAndGet();
        System.out.println("订单" + orderId + "已断开，当前活跃连接数：" + activeConnectionCount.get());
    }




    // 自动接收消息，框架异步调用，不用自己开线程
    //WebSocket 框架（SpringBoot 自带）底层已经封装好异步监听线程
    //客户端发消息过来 → 框架自动回调 onMessage()
    //你直接在 onMessage 里写业务就行，不用新建线程轮询接收
    @OnMessage
    public void onMessage(String msg, Session session) {

        System.out.println("收到小程序消息：" + msg);
        //对发来的JSON消息进行转
        PaySend orderMsg = JSON.parseObject(msg, PaySend.class);
        switch (orderMsg.getType()) {
            case 1:
                // 历史订单进来的
                // 调用连接到的二维码支付页面websocket给他发消息
                ChatWebSocket.sendToLastSocket(orderMsg);
                break;

            default:
                break;
        }

    }
}