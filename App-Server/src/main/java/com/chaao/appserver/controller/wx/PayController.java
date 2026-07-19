package com.chaao.appserver.controller.wx;


import com.chaao.appserver.mapper.wx.PayLogMapper;
import com.chaao.appserver.service.Impl.wx.OrderPayService;
import com.chaao.appserver.service.Impl.wx.OrderWebSocketServer;
import dto.wx.PayOrderDTO;
import dto.wx.RefundDTO;
import entity.wx.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;
import vo.wx.PayRespVO;

@Slf4j
@RestController
@RequestMapping("/wx/pay")
public class PayController {

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private PayLogMapper payLogMapper;


    /**
     *   给扫码的网站查询出这个用户最后一条订单
     */
    @GetMapping("/query")
    public Result queryPay(@RequestParam Long userId){
        Orders order = payLogMapper.getLastOrderInfoByUserId(userId);
        return Result.success(order);
    }



    /**
     *    通知小程序用户已经扫码了
     *    扫码的浏览器调用这个接口 这个接口里面用websocket 给小程序发送消息
     */
    @GetMapping("/notify")
    public Result notifyPay(@RequestParam String orderId){
        String jsonMsg = "{\"type\":\"scan\",\"msg\":\"用户已扫码，请完成确认支付\"}";
        OrderWebSocketServer.sendMsg(orderId, jsonMsg);
        return Result.success("推送成功");
    }


    /**
     * 关闭浏览器/退出页面
     * websocket 给小程序发送消息发送取消支付/支付失败
     */
    @GetMapping("/close")
    public Result closePay(@RequestParam String orderId){
        log.info("用户主动退出支付");
        String jsonMsg = "{\"type\":\"close\",\"msg\":\"用户主动退出支付，已取消本次订单支付\"}";
        OrderWebSocketServer.sendMsg(orderId, jsonMsg);
        return Result.success("推送成功");
    }


    /**
     * 【新增】心跳接口，前端页面定时调用刷新在线状态
     */
    @GetMapping("/heart")
    public Result heartBeat(@RequestParam String orderId){
        OrderWebSocketServer.refreshActive(orderId);
        return Result.success();
    }


    /**
     * 发起支付接口
     */
    @PostMapping("/create")
    public PayRespVO createPay(@RequestBody PayOrderDTO payOrderDTO){
        return orderPayService.createOrderPay(payOrderDTO);
    }

    /**
     * 订单退款接口
     */
    @PostMapping("/refund")
    public PayRespVO orderRefund(@RequestBody RefundDTO refundDTO){
        return orderPayService.orderRefund(refundDTO);
    }
}