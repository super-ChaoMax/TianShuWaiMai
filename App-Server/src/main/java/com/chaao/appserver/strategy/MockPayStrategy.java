package com.chaao.appserver.strategy;


import dto.wx.PayOrderDTO;
import dto.wx.RefundDTO;
import org.springframework.stereotype.Component;
import vo.wx.PayRespVO;

@Component
public class MockPayStrategy implements PayStrategy {

    // Mock渠道标识
    public static final String CHANNEL_MOCK = "MOCK";

    @Override
    public String getChannelCode() {
        return CHANNEL_MOCK;
    }

    @Override
    public PayRespVO createPay(PayOrderDTO dto) {
        PayRespVO vo = new PayRespVO();
        vo.setPayUrl("/pay/mock?orderNo=" + dto.getOrderNo());
        vo.setPayChannel(CHANNEL_MOCK);
        vo.setPayStatus("CREATE");
        vo.setOutTradeNo("MOCK_"+System.currentTimeMillis());
        vo.setBody("MOCK模拟下单成功");
        return vo;
    }

    @Override
    public PayRespVO queryPayStatus(String orderNo) {
        PayRespVO vo = new PayRespVO();
        vo.setPayChannel(CHANNEL_MOCK);
        vo.setPayStatus("CREATE");
        vo.setBody("模拟订单待用户支付");
        return vo;
    }

    @Override
    public PayRespVO refund(RefundDTO dto) {
        PayRespVO vo = new PayRespVO();
        vo.setPayChannel(CHANNEL_MOCK);
        vo.setOutTradeNo(dto.getOutTradeNo());
        vo.setPayStatus("SUCCESS");
        vo.setBody("MOCK模拟退款完成");
        return vo;
    }

    @Override
    public String notifyHandler(String params) {
        // 模拟回调直接返回成功
        return "success";
    }
}