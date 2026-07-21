package com.chaao.appserver.service.Impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.chaao.appserver.mapper.OrderStatusLogMapper;
import com.chaao.appserver.service.OrderStatusLogService;
import entity.OrderStatusLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderStatusLogServiceImpl implements OrderStatusLogService {

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    private static final Snowflake SNOW = IdUtil.getSnowflake(1,1);

    @Override
    public void saveStatusLog(Long orderId, Integer fromStatus, Integer toStatus,
                              Integer operatorType, Long operatorId, String remark) {
        OrderStatusLog log = new OrderStatusLog();
        log.setId(SNOW.nextId());
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setOperatorType(operatorType);
        log.setOperatorId(operatorId);
        log.setRemark(remark);
        orderStatusLogMapper.insert(log);
    }

    @Override
    public List<OrderStatusLog> getLogByOrderId(Long orderId) {
        return orderStatusLogMapper.getListByOrderId(orderId);
    }

//// 1.先查旧状态
//Integer oldStatus = orderStatusLogMapper.getLastOrderStatus(orderId);
//// 2.更新订单主表状态
//orders.setStatus(newStatus);
//orderMapper.updateStatus(orders);
//// 3.写入状态流水
//orderStatusLogService.saveStatusLog(
//        orderId,
//        oldStatus,
//        newStatus,
//        4,          // 4=系统操作
//        null,
//        "超时未支付自动取消订单"
//);



}