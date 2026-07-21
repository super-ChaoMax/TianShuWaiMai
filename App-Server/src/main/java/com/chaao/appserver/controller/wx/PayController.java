package com.chaao.appserver.controller.wx;


import com.chaao.appserver.mapper.wx.PayLogMapper;
import com.chaao.appserver.service.Impl.OrderStatusLogServiceImpl;
import com.chaao.appserver.service.Impl.wx.OrderPayService;
import com.chaao.appserver.service.Impl.wx.OrderServiceImpl;
import com.chaao.appserver.service.Impl.wx.OrderWebSocketServer;
import com.chaao.appserver.service.Impl.wx.ShoppingCartServiceImpl;
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

    //购物车
    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    //订单
    @Autowired
    private OrderServiceImpl orderService;

    //订单修改日志
    @Autowired
    private OrderStatusLogServiceImpl orderLogService;


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
     * 【新增逻辑】扫码成功后修改当前订单心跳超时时间为5秒
     */
    @GetMapping("/notify")
    public Result notifyPay(@RequestParam String orderId){
        log.info("用户扫码成功，将订单心跳超时由默认20秒改为5秒");
        //【新增】扫码完成，将订单心跳超时由默认20秒改为5秒
        OrderWebSocketServer.setHeartTimeout(orderId,5*1000);
        // 2. 立刻刷新活跃时间，重置计时起点（防止刚改就超时）
        OrderWebSocketServer.refreshActive(orderId);
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
        // 加这一行！！！扫码瞬间强制刷新活跃时间，清零计时
        OrderWebSocketServer.refreshActive(orderId);

        String jsonMsg = "{\"type\":\"close\",\"msg\":\"用户主动退出支付，已取消本次订单支付\"}";
        OrderWebSocketServer.sendMsg(orderId, jsonMsg);
        return Result.success("推送成功");
    }


    /**
     * 【新增】心跳接口，前端页面定时调用刷新在线状态
     */
    @GetMapping("/heart")
    public Result heartBeat(@RequestParam String orderId){
        log.info("用户刷新在线状态");
        OrderWebSocketServer.refreshActive(orderId);
        return Result.success();
    }


    /**
     * 发起支付接口（直接就是支付成功）
     */
    @PostMapping("/create")
    public PayRespVO createPay(@RequestBody PayOrderDTO payOrderDTO){
        log.info("用户发起支付");
        String jsonMsg = "{\"type\":\"paySuccess\",\"msg\":\"用户支付成功\"}";
        OrderWebSocketServer.sendMsg(payOrderDTO.getOrderNo(), jsonMsg);

        //清除购物车
        shoppingCartService.deleteByUserId(payOrderDTO.getUserId());

        //修改订单状态（待配送）
            //string转换成LONG
        orderService.updateStatus(Long.parseLong(payOrderDTO.getOrderNo()), 2);// 修改订单状态为待配送

        //并且添加订单修改日志
        orderLogService.saveStatusLog(
                Long.parseLong(payOrderDTO.getOrderNo()),
                1,
                2,
                4,
                payOrderDTO.getUserId(),
                "用户主动发起支付成功"
        );

        return orderPayService.createOrderPay(payOrderDTO);
    }

//    /**
//     * 订单退款接口
//     */
//    @PostMapping("/refund")
//    public PayRespVO orderRefund(@RequestBody RefundDTO refundDTO){
//        return orderPayService.orderRefund(orderRefundDTO);
//    }
}