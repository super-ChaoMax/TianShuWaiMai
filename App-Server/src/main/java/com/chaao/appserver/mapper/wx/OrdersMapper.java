package com.chaao.appserver.mapper.wx;

import entity.wx.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {

    /**
     * 插入一条新订单记录
     * @param orders 订单实体对象
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单（用于支付回调或详情展示）
     */
    Orders getByNumber(String number);
}