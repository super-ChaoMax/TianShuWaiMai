package com.chaao.appserver.controller;


import cn.hutool.log.Log;
import com.chaao.appserver.service.Impl.AWOrderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;
import vo.admin.OrderInfoVO;

@Slf4j
@RestController
@RequestMapping("/aw/orders")
@Tag(name = "AC端-")
public class AWOrderController {


    @Autowired
    AWOrderServiceImpl awOrderService;

    // 去掉@PathVariable，用@RequestParam接收字符串
    @PostMapping
    public Result<OrderInfoVO> getOrderInfoVO(@RequestBody String orderIdStr){
        log.info("收到完整订单ID：{}", orderIdStr);
        Long id = Long.parseLong(orderIdStr);
        OrderInfoVO vo = awOrderService.getOrderInfoVO(id);
        if(vo == null){
            return Result.error("订单不存在");
        }
        return Result.success(vo);
    }



}
