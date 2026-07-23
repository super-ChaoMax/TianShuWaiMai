package com.chaao.appserver.task;

import com.chaao.appserver.websocket.pay.ChatWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vo.PaySend;

@EnableScheduling
@Component
@Slf4j
public class WebSocketPushTask {

    // 全局开关：true开启推送 false关闭
    public static boolean pushSwitch = false;
    // 动态传递订单号
    public static String targetOrderId;
    // 动态type
    public static Integer pushType;

    // 3秒执行一次
    @Scheduled(fixedRate = 1000)
    public void checkAndPushMsg() {

        // 开关关闭直接跳过
        if (!pushSwitch) {
            return;
        }
        // 无订单号也不推送
        if (targetOrderId == null || targetOrderId.isEmpty() ) {
            return;
        }
        // 无在线连接跳过
        if (ChatWebSocket.SOCKET_LIST.isEmpty()) {
            return;
        }

        // 组装动态参数消息
        PaySend sendVo = new PaySend();
        sendVo.setType(pushType);
        sendVo.setOrderNo(targetOrderId);
        log.info("开始推送消息给二维码网站"+sendVo);

        // 推送
        ChatWebSocket.sendToLastSocket(sendVo);
    }

    // ========== 对外控制方法 ==========
    /**
     * 开启定时推送
     * @param orderId 目标订单号
     * @param type 消息类型
     */
    public void startPush(String orderId, Integer type) {
        this.targetOrderId = orderId;
        this.pushType = type;
        WebSocketPushTask.pushSwitch = true;
    }

    /**
     * 关闭定时推送
     */
    public void stopPush() {
        WebSocketPushTask.pushSwitch = false;
        // 可选：清空参数
        this.targetOrderId = null;
        this.pushType = null;
    }
}