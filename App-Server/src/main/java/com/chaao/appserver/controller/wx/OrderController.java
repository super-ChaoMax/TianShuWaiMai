package com.chaao.appserver.controller.wx;


import com.chaao.appserver.service.OrderService;
import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.util.SecurityUtils;
import dto.wx.OrderSubmitDTO;
import entity.wx.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;
import vo.wx.OrderVO;

/**
 * 订单管理控制器
 */
@RestController("userOrderController") // 区分后台管理的 OrderController
@RequestMapping("/wx/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单接口
     * 核心逻辑：后端根据 cartIds 去数据库查购物车数据组装订单
     */
    @PostMapping("/submit")
    public Result<OrderVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO) throws Exception {
        log.info("用户下单，参数为：{}", orderSubmitDTO);

        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键

            orderSubmitDTO.setUserId(userId);



            // 2. 调用 Service 层执行复杂的下单逻辑（含事务）
            // 这里会处理库存扣减、金额计算、餐具信息保存等
            OrderVO orderVO = orderService.submitOrder(orderSubmitDTO);

            return Result.success(orderVO);
        }
        throw new Exception("非微信用户无法清空购物车");


    }
}