package com.chaao.appserver.factory;


import com.chaao.appserver.strategy.PayStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//支付工厂（核心：自动匹配渠道，业务只调用工厂，不依赖具体实现）
@Component
public class PayStrategyFactory {

    // 缓存所有支付渠道实现类 key=渠道编码 value=实现对象
    private final Map<String, PayStrategy> strategyMap = new HashMap<>();

    // 项目启动时，自动注入所有实现PayStrategy的Bean存入map
    private final List<PayStrategy> payStrategyList;
    public PayStrategyFactory(List<PayStrategy> payStrategyList) {
        this.payStrategyList = payStrategyList;
    }

    @PostConstruct
    public void init() {
        for (PayStrategy strategy : payStrategyList) {
            strategyMap.put(strategy.getChannelCode(), strategy);
        }
    }

    /**
     * 根据渠道编码获取对应支付实现
     */
    public PayStrategy getStrategy(String channelCode) {
        PayStrategy strategy = strategyMap.get(channelCode);
        if (strategy == null) {
            throw new RuntimeException("不支持的支付渠道：" + channelCode);
        }
        return strategy;
    }
}