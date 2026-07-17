package com.chaao.appserver.service;

import dto.wx.OrderSubmitDTO;
import vo.wx.OrderVO;

public interface OrderService {

    //submitOrder
    OrderVO submitOrder(OrderSubmitDTO orderSubmitDTO);

}
