package com.chaao.appserver.service.Impl.wx;

import com.chaao.appserver.factory.PayStrategyFactory;

import com.chaao.appserver.service.Impl.wx.PayLogService;
import com.chaao.appserver.strategy.PayStrategy;

import dto.wx.PayOrderDTO;
import dto.wx.RefundDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.wx.PayRespVO;


@Service
public class OrderPayService {

    // 支付策略工厂
    @Autowired
    private PayStrategyFactory payStrategyFactory;

    // 支付日志服务
    @Autowired
    private PayLogService payLogService;


    // ===================== 1. 发起支付 =====================
    public PayRespVO createOrderPay(PayOrderDTO dto) {
        // 1. 业务校验（订单状态、金额、用户等）

        // 2. 获取对应支付渠道策略
        PayStrategy payStrategy = payStrategyFactory.getStrategy(dto.getPayChannel());

        // 3. 执行发起支付
        PayRespVO payResp = payStrategy.createPay(dto);

        // 4. 保存支付日志
        payLogService.savePayLog(dto, payResp);

        return payResp;
    }


    // ===================== 2. 订单退款 =====================
    public PayRespVO orderRefund(RefundDTO refundDTO){
        // 1. 退款前置校验（订单是否可退、金额是否合法）

        // 2. 获取对应渠道退款策略
        PayStrategy strategy = payStrategyFactory.getStrategy(refundDTO.getPayChannel());

        // 3. 执行退款
        PayRespVO resp = strategy.refund(refundDTO);

        // 4. 保存退款日志
        payLogService.saveRefundLog(refundDTO,resp);

        return resp;
    }

}