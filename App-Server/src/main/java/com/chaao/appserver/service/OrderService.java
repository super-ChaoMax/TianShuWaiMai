package com.chaao.appserver.service;


import dto.wx.OrderPageQueryDTO;
import dto.wx.OrderSubmitDTO;
import vo.PageResult;
import vo.wx.OrderUserVO;
import vo.wx.OrderVO;

public interface OrderService {

    //submitOrder（提交订单）
    OrderVO submitOrder(OrderSubmitDTO orderSubmitDTO);


    //修改订单状态
    void updateStatus(Long orderId, Integer status);

    /**
     * 用户分页查询历史订单
     */
    PageResult<OrderUserVO> pageList(Long userId, OrderPageQueryDTO dto);

}
