package com.chaao.appserver.task;

import com.chaao.appserver.service.Impl.wx.OrderWebSocketServer;
import jakarta.websocket.Session;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PayHeartTimeoutTask {

    @Scheduled(fixedRate = 2000)
    public void checkPayTimeout() {
        if (OrderWebSocketServer.activeConnectionCount.get() == 0) {
            return;
        }

        System.out.println("【定时任务】开始检查订单支付超时，当前活跃连接数：" + OrderWebSocketServer.activeConnectionCount.get());
        long currentTime = System.currentTimeMillis();
        var activeMap = OrderWebSocketServer.activeTimeMap;
        var iterator = activeMap.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            String orderId = entry.getKey();
            long lastActiveTime = entry.getValue();
            long timeout = OrderWebSocketServer.orderTimeoutMap.getOrDefault(orderId, 20 * 1000L);

            if (currentTime - lastActiveTime > timeout) {
                String timeoutMsg = "{\"type\":\"close\",\"msg\":\"长时间未操作，自动取消支付\"}";
                OrderWebSocketServer.sendMsg(orderId, timeoutMsg);

                // 拿到会话
                Session session = OrderWebSocketServer.orderSessionMap.get(orderId);
                if(session != null && session.isOpen()){
                    try {
                        // 主动关闭连接 → 自动触发onClose → 自动减计数器
                        session.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 清理缓存数据
                iterator.remove();
                OrderWebSocketServer.orderTimeoutMap.remove(orderId);
                OrderWebSocketServer.orderSessionMap.remove(orderId);

                System.out.println("订单：" + orderId + " 支付超时自动取消");
            }
        }
    }
}