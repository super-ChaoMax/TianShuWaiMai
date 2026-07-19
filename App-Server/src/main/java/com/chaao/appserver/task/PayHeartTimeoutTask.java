package com.chaao.appserver.task;

import com.chaao.appserver.service.Impl.wx.OrderWebSocketServer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 【新增】支付心跳超时定时检测
 * 注解解析：
 * @Component 交给Spring容器管理
 * @EnableScheduling 开启Spring定时任务功能（全局只需要一次）
 * @Scheduled(fixedRate = 2000) 固定每2秒执行一次检测
 */
@Component
@EnableScheduling
public class PayHeartTimeoutTask {

    @Scheduled(fixedRate = 2000)
    public void checkPayTimeout() {
        long currentTime = System.currentTimeMillis();
        // 遍历所有存在活跃记录的订单
        for (Map.Entry<String, Long> entry : OrderWebSocketServer.activeTimeMap.entrySet()) {
            String orderId = entry.getKey();
            long lastActiveTime = entry.getValue();

            // 判断是否超过5秒无心跳
            if (currentTime - lastActiveTime > OrderWebSocketServer.HEART_TIMEOUT) {
                // 拼接取消支付推送消息
                String timeoutMsg = "{\"type\":\"close\",\"msg\":\"长时间未操作，自动取消支付\"}";
                // 推送消息到小程序
                OrderWebSocketServer.sendMsg(orderId, timeoutMsg);
                // 移除已超时订单，防止重复推送
                OrderWebSocketServer.activeTimeMap.remove(orderId);
                System.out.println("订单：" + orderId + " 支付超时自动取消");
            }
        }
    }
}