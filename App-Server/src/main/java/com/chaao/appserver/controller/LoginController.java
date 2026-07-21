package com.chaao.appserver.controller;

import com.chaao.appserver.service.SpringSecurity.LoginUser;
import com.chaao.appserver.service.SpringSecurity.UserDetailServiceImpl;
import com.chaao.appserver.service.WxUserService;
import com.chaao.appserver.util.JwtUtil;
import com.chaao.appserver.util.WxApiUtil;
import dto.LoginDTO;
import dto.WxLoginDTO;
import entity.wx.WxUser;
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
import vo.admin.AdminVO;

import java.util.HashMap;
import java.util.Map;

//接口文档地址：http://localhost:8080/swagger-ui/index.html

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
    public Result adminLogin(@RequestBody  @ApiParam("登录参数") LoginDTO dto){
    /*
你调用 authenticate(账号+明文密码)
SpringSecurity 内部自动去找项目中唯一实现 UserDetailsService 的类
自动执行：userDetailsService.loadUserByUsername(前端用户名)
走到你写的方法里：根据用户名查数据库用户、拿到加密密码
框架拿到你返回的 LoginUser
框架自动拿全局 PasswordEncoder
把前端明文密码加密，和你返回的数据库密文对比
匹配成功 = 认证通过
匹配失败 = 直接抛 用户名或密码错误

     */



        try {
            // 标记管理员登录类型
            UserDetailServiceImpl.LOGIN_TYPE.set(1);

            Authentication auth = authenticationManager.authenticate(
                    // 封装用户名和密码
                    //并且Security 自动调用 userDetailsService.loadUserByUsername(用户名)
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );


            LoginUser loginUser = (LoginUser) auth.getPrincipal();
            log.info("管理员登录成功：{}", loginUser);
            // 生成JWT
            String token = jwtUtil.generateLoginToken(
                    loginUser.getUsername(),
                    loginUser.getSysUser().getId(),
                    loginUser.getUserType()
            );
//            Map<String,Object> res = new HashMap<>();
//            res.put("code",200);
//            res.put("msg","登录成功");
//            res.put("token",token);
//            res.put("userId",loginUser.getSysUser().getId());
//            res.put("userType",1);
            AdminVO adminVO = new AdminVO();
            adminVO.setId(loginUser.getSysUser().getId());
            adminVO.setUsername(loginUser.getUsername());
            adminVO.setName(loginUser.getSysUser().getName());
            adminVO.setToken(token);
            return Result.success(1,"登录成功", adminVO);
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