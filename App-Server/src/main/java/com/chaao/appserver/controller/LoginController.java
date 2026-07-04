package com.chaao.appserver.controller;

import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.service.SpringSecurity.UserDetailServiceImpl;
import com.chaao.appserver.service.WxUserService;
import com.chaao.appserver.util.JwtUtil;
import com.chaao.appserver.util.WxApiUtil;
import dto.LoginDTO;
import dto.WxLoginDTO;
import entity.wx.WxUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vo.Result;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "登录接口模块（C端微信用户/后台）")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WxUserService wxUserService;

    // 后台管理员登录
    @PostMapping("/admin/login")
    @Operation(summary ="管理员账号密码登录")
    public Map<String,Object> adminLogin(@RequestBody  @ApiParam("登录参数") LoginDTO dto){
        try {
            // 标记管理员登录类型
            UserDetailServiceImpl.LOGIN_TYPE.set(1);
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            LoginUser loginUser = (LoginUser) auth.getPrincipal();
            // 生成JWT
            String token = jwtUtil.generateLoginToken(
                    loginUser.getUsername(),
                    loginUser.getSysUser().getId(),
                    loginUser.getUserType()
            );
            Map<String,Object> res = new HashMap<>();
            res.put("code",200);
            res.put("msg","登录成功");
            res.put("token",token);
            res.put("userId",loginUser.getSysUser().getId());
            res.put("userType",1);
            return res;
        }finally {
            // 必须清空，防止污染
            UserDetailServiceImpl.LOGIN_TYPE.remove();
        }
    }

    @Autowired
    private WxApiUtil wxApiUtil;

    // 微信小程序登录
    @PostMapping("/wx/login")
    @Operation( summary  ="微信小程序零时Code登录")
    public Result wxLogin(@RequestBody  @ApiParam("微信登录参数") WxLoginDTO dto) {
        // 校验前端传过来的code
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            return Result.error("登录授权码不能为空");
        }
        try {
            // 1.调用微信接口获取openid
            String openid = wxApiUtil.getOpenId(dto.getCode());
            // 2.根据openid查询微信用户
            WxUser wxUser = wxUserService.getUserByOpenId(openid);
            // 3.不存在则自动注册
            if (wxUser == null) {
                wxUser = wxUserService.registerWxUser(openid);
            }
            // 4.直接生成微信用户专属JWT令牌
            String token = jwtUtil.generateWxToken(wxUser.getId(), openid);
            return Result.success("登录成功", token);
        } catch (Exception e) {
            log.error("微信登录异常：", e);
            return Result.error("微信登录失败");
        }
    }



}