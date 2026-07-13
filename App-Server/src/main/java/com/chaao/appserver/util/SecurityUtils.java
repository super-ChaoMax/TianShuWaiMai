package com.chaao.appserver.util;


import com.chaao.appserver.service.SpringSecurity.LoginUser;
import entity.rbac.SysUser;
import entity.wx.WxUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *      封装 Spring Security 的工具类
 *      获取当前登录用户信息，就不用一个一个调用直接调用这个方法就行了
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户的 LoginUser 对象
     */
    public static LoginUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 【核心修改】获取当前登录用户的 ID (Object类型，兼容String和Long)
     */
    public static Object getCurrentUserId() {
        LoginUser loginUser = getCurrentUser();
        if (loginUser == null) {
            return null;
        }

        // 情况A：如果是管理员，取 SysUser 的 id
        if (loginUser.isAdmin()) {
            SysUser sysUser = loginUser.getSysUser();
            return sysUser != null ? sysUser.getId() : null; 
        } 
        
        // 情况B：如果是微信用户，取 WxUser 的 id
        else if (loginUser.isWxUser()) {
            WxUser wxUser = loginUser.getWxUser();
            // 注意：这里假设 WxUser 的主键叫 getId()，如果是其他名字请调整
            return wxUser != null ? wxUser.getId() : null; 
        }

        return null;
    }
}