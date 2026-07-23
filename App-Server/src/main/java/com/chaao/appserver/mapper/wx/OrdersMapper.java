package com.chaao.appserver.mapper.wx;

import entity.wx.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper {

    /**
     * 插入一条新订单记录
     * @param orders 订单实体对象
     */
    void insert(Orders orders);

    /**
     * 根据订单ID修改订单状态（用于支付回调或订单状态更新）
     */
    void updateStatus(Long orderId, Integer status);


    /**
     * 分页查询当前用户订单
     */
    List<Orders> selectUserOrderPage(@Param("userId") Long userId,
                                     @Param("status") Integer status,
                                     @Param("offset") Integer offset,
                                     @Param("pageSize") Integer pageSize);

    /**
     * 统计当前用户订单总数
     */
    Long countUserOrder(@Param("userId") Long userId, @Param("status") Integer status);


    // 根据id去查
    Orders getById(@Param("id") Long id);


    /**
     * 根据订单号查询订单（用于支付回调或详情展示）
     */
    Orders getByNumber(String number);
}