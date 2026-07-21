package com.chaao.appserver.mapper;

import entity.OrderStatusLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderStatusLogMapper {

    /**
     * 新增订单状态流转日志
     */
    int insert(OrderStatusLog log);

    /**
     * 根据订单ID查询全量状态流转记录
     */
    List<OrderStatusLog> getListByOrderId(@Param("orderId") Long orderId);

    /**
     * 获取订单上一次状态
     */
    Integer getLastOrderStatus(@Param("orderId") Long orderId);
}