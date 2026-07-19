package com.chaao.appserver.service;

import dto.wx.OrderSubmitDTO;
import vo.wx.OrderVO;

public interface OrderService {

    //submitOrder（提交订单）
    OrderVO submitOrder(OrderSubmitDTO orderSubmitDTO);



}
