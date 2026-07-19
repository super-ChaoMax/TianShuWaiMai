package com.chaao.appserver.service.Impl.wx;


import com.chaao.appserver.mapper.wx.PayLogMapper;
import dto.wx.PayOrderDTO;
import dto.wx.RefundDTO;
import log.wx.PayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.wx.PayRespVO;

import java.util.Date;

@Service
public class PayLogService {

    // 统一使用 @Autowired 注入
    @Autowired
    private PayLogMapper payLogMapper;

    /**
     * 保存退款日志
     */
    public void saveRefundLog(RefundDTO dto, PayRespVO resp) {
        PayLog payLog = new PayLog();
        payLog.setOrderNo(dto.getOrderNo());
        payLog.setOutTradeNo(dto.getOutTradeNo());
        payLog.setRefundNo(dto.getRefundNo());
        payLog.setAmount(dto.getRefundAmount());
        payLog.setPayChannel(dto.getPayChannel());
        payLog.setLogType(2); // 2=退款
        payLog.setStatus(resp.isSuccess() ? 1 : 0);
        payLog.setResultContent(resp.getBody());
        payLog.setCreateTime(new Date());
        // 原生mybatis插入
        payLogMapper.insertPayLog(payLog);
    }

    /**
     * 保存支付日志（预留）
     */
    public void savePayLog(PayOrderDTO dto, PayRespVO resp) {
        PayLog payLog = new PayLog();
        payLog.setOrderNo(dto.getOrderNo());
        payLog.setOutTradeNo(resp.getOutTradeNo());
        payLog.setAmount(dto.getAmount());
        payLog.setPayChannel(dto.getPayChannel());
        payLog.setLogType(1); // 1=支付
        payLog.setStatus(resp.isSuccess() ? 1 : 0);
        payLog.setResultContent(resp.getBody());
        payLog.setCreateTime(new Date());
        payLogMapper.insertPayLog(payLog);
    }


}