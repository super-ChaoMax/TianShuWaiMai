package com.chaao.appserver.service;

import entity.OrderStatusLog;

import java.util.List;

public interface OrderStatusLogService {

    /**
     * 保存订单状态变更日志
     * @param orderId 订单id
     * @param fromStatus 原状态
     * @param toStatus 新状态
     * @param operatorType 操作人类型
     * @param operatorId 操作人id
     * @param remark 备注
     */
    void saveStatusLog(Long orderId, Integer fromStatus, Integer toStatus,
                       Integer operatorType, Long operatorId, String remark);

    /**
     * 查询订单状态流转记录
     */
    List<OrderStatusLog> getLogByOrderId(Long orderId);
}