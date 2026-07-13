package com.chaao.appserver.controller.wx;

import com.chaao.appserver.service.ShoppingCartService;
import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.util.SecurityUtils;
import dto.wx.ShoppingCartDTO;
import entity.wx.ShoppingCart;
import entity.wx.WxUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.Result;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wx/shoppingCart")
@Tag(name = "C端-购物车接口")
public class ShoppingCartController {


    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public Result<List<ShoppingCart>> selectByUserIdList() throws Exception {

        System.out.println("------------- 开始请求C端-购物车接口 ----------");
        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键

            // 执行 SQL 查询...
            return Result.success(shoppingCartService.getShoppingCartList(userId));
        }
        throw new Exception("非微信用户无法查看购物车");


    }


    @PostMapping("/add")
    public Result<?> add( @RequestBody   ShoppingCartDTO shoppingCartDTO) throws Exception {
        log.info("前端传递的参数" + shoppingCartDTO);
        System.out.println("------------- 添加购物车 ----------");
        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键
            // 执行 SQL 查询...
            shoppingCartService.add(userId, shoppingCartDTO);
            return Result.success();
        }
        throw new Exception("非微信用户无法查看购物车");
    }



    @PostMapping("/sub")
    public Result<?> sub( @RequestBody   ShoppingCartDTO shoppingCartDTO) throws Exception {
        log.info("前端传递的参数" + shoppingCartDTO);
        System.out.println("------------- 减少数量 ----------");
        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键
            // 执行 SQL 查询...
            shoppingCartService.subNumberById(userId, shoppingCartDTO);
            return Result.success();
        }
        throw new Exception("非微信用户无法查看购物车");
    }



    @DeleteMapping("/clean")
    public Result<?> deleteByUserId() throws Exception {
        System.out.println("------------- 清空购物车 ----------");
        LoginUser loginUser = SecurityUtils.getCurrentUser();

        // 强制转换为微信用户逻辑
        if (loginUser != null && loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            Long userId = wxUser.getId(); // 拿到微信用户的数据库主键
            // 执行 SQL 删除...
            shoppingCartService.deleteByUserId(userId);
            return Result.success();
        }
        throw new Exception("非微信用户无法清空购物车");
    }




}
