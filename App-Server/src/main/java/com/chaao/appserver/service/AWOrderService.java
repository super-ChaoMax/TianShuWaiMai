package com.chaao.appserver.service;

import vo.admin.OrderInfoVO;

public interface AWOrderService {

    //查询订单详情
    OrderInfoVO getOrderInfoVO(Long id);

}
