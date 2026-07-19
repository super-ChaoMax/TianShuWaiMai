package com.chaao.appserver.task;

import com.chaao.appserver.service.Impl.wx.OrderWebSocketServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

                iterator.remove();
                OrderWebSocketServer.orderTimeoutMap.remove(orderId);
                OrderWebSocketServer.orderSessionMap.remove(orderId);

                // ====================== 补上这行！！！ ======================
                // 订单超时失效，连接计数必须减1
                OrderWebSocketServer.activeConnectionCount.decrementAndGet();

                System.out.println("订单：" + orderId + " 支付超时自动取消");
            }
        }
    }
}