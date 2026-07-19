package com.chaao.appserver.task;

import com.chaao.appserver.service.Impl.wx.OrderWebSocketServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Iterator;
import java.util.Map;

@Component
// 重要：@EnableScheduling 只能在启动类上加一次，这里要删掉！
public class PayHeartTimeoutTask {

    @Scheduled(fixedRate = 2000)
    public void checkPayTimeout() {

        System.out.println("【定时任务】开始检查订单支付超时");
        long currentTime = System.currentTimeMillis();
        Map<String, Long> activeMap = OrderWebSocketServer.activeTimeMap;

        // 使用迭代器进行安全遍历和删除
        Iterator<Map.Entry<String, Long>> iterator = activeMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            String orderId = entry.getKey();
            long lastActiveTime = entry.getValue();

            // 获取该订单专属的超时时间
            long timeout = OrderWebSocketServer.orderTimeoutMap.getOrDefault(orderId, 20 * 1000L);

            if (currentTime - lastActiveTime > timeout) {
                // 1. 推送超时关闭消息给小程序
                String timeoutMsg = "{\"type\":\"close\",\"msg\":\"长时间未操作，自动取消支付\"}";
                OrderWebSocketServer.sendMsg(orderId, timeoutMsg);

                // 2. 安全移除当前订单（这一步是关键，解决并发异常）
                iterator.remove();
                // 3. 同时清理其他相关缓存
                OrderWebSocketServer.orderTimeoutMap.remove(orderId);
                OrderWebSocketServer.orderSessionMap.remove(orderId);

                System.out.println("订单：" + orderId + " 支付超时自动取消");
            }
        }
    }
}