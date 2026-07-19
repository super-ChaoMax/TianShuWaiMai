package com.chaao.appserver.strategy;


import dto.wx.PayOrderDTO;
import dto.wx.RefundDTO;
import vo.wx.PayRespVO;

//顶层抽象支付接口 PayStrategy（策略接口，所有渠道实现）
public interface PayStrategy {

    /**
     * 获取当前渠道标识（工厂用来匹配实现类）
     */
    String getChannelCode();

    /**
     * 创建支付单，拉起支付
     */
    PayRespVO createPay(PayOrderDTO dto);

    /**
     * 查询订单支付状态
     */
    PayRespVO queryPayStatus(String orderNo);

    /**
     * 统一退款
     */
    PayRespVO refund(RefundDTO dto);

    /**
     * 支付异步回调验签、解析参数
     */
    String notifyHandler(String params);
}