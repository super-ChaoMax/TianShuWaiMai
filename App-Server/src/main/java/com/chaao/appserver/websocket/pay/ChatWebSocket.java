package com.chaao.appserver.websocket.pay;

import com.alibaba.fastjson2.JSON;
import com.chaao.appserver.task.WebSocketPushTask;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vo.PaySend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@ServerEndpoint("/ws/aw/pay")
public class ChatWebSocket {

    // 【关键】用静态变量保存推送任务实例
    private static WebSocketPushTask staticWebSocketPushTask;

    // 【关键】通过 setter 注入，解决静态注入问题
    @Autowired
    public void setWebSocketPushTask(WebSocketPushTask task) {
        staticWebSocketPushTask = task;
    }


    // 存所有在线会话，按连接先后排序
    public static final List<ChatWebSocket> SOCKET_LIST = new CopyOnWriteArrayList<>();

    private Session webSession;

    @OnOpen
    public void onOpen(Session session) {
        this.webSession = session;
        SOCKET_LIST.add(this);
        log.info("-------客户端接入，当前在线总数：{}----------", SOCKET_LIST.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端消息：{}",message);
    }

    @OnClose
    public void onClose() {
        SOCKET_LIST.remove(this);
        log.info("客户端断开，剩余在线：{}", SOCKET_LIST.size());
    }

    // 单发当前这个会话
    private void sendMsg(PaySend paySend) {
        try {
            if (webSession != null && webSession.isOpen()) {
                String json = JSON.toJSONString(paySend);
                webSession.getBasicRemote().sendText(json);
            }
        } catch (Exception e) {
            log.error("推送消息失败",e);
        }
    }

    /**
     * 直接发给【最后一个连接进来】的客户端
     * 不需要传任何 sessionId / clientId
     */
    public static void sendToLastSocket(PaySend paySend){
        if(SOCKET_LIST.isEmpty()){
            log.warn("暂无在线WebSocket连接，已准备开启定时器查看");
            // 现在用静态变量调用，不会报错
            if(staticWebSocketPushTask != null){
                staticWebSocketPushTask.startPush(paySend.getOrderNo(), paySend.getType());
            }
            return;
        }
        // 取最后一位
        ChatWebSocket last = SOCKET_LIST.get(SOCKET_LIST.size() - 1);
        last.sendMsg(paySend);
        log.warn("有在线的已推送关闭定时器");
        staticWebSocketPushTask.stopPush();

    }
}