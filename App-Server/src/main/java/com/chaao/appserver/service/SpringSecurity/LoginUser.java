package com.chaao.appserver.service.SpringSecurity;

import entity.rbac.SysUser;
import entity.wx.WxUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 全端统一登录用户包装类
 * 持有真实业务实体：管理员SysUser / 微信用户WxUser
 */
@Data
public class LoginUser implements UserDetails {

    /**
     * 用户类型 1=管理员 2=微信普通用户
     */
    private Integer userType;

    /**
     * 真实业务用户实体
     */
    private final Object user;

    /**
     * 权限集合
     */
    private final List<GrantedAuthority> authorities;

    // 通用构造
    public LoginUser(Object user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    // ========== 快捷静态构建方法（业务层直接调用） ==========
    /**
     * 构建管理员登录用户
     */
    public static LoginUser buildAdmin(SysUser sysUser, List<GrantedAuthority> auths) {
        LoginUser loginUser = new LoginUser(sysUser, auths);
        loginUser.setUserType(1);
        return loginUser;
    }

    /**
     * 构建微信小程序用户
     */
    public static LoginUser buildWxUser(WxUser wxUser, List<GrantedAuthority> auths) {
        LoginUser loginUser = new LoginUser(wxUser, auths);
        loginUser.setUserType(2);
        return loginUser;
    }

    // ========== 类型强转工具方法（业务获取真实实体） ==========
    public SysUser getSysUser() {
        if (user != null && user instanceof SysUser) {
            return (SysUser) user;
        }
        return null;
    }

    public WxUser getWxUser() {
        if (user != null && user instanceof WxUser) {
            return (WxUser) user;
        }
        return null;
    }

    public boolean isAdmin() {
        return Integer.valueOf(1).equals(userType);
    }

    public boolean isWxUser() {
        return Integer.valueOf(2).equals(userType);
    }

    // ========== 重写Security UserDetails 必实现方法 ==========
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities == null ? Collections.emptyList() : this.authorities;
    }

    /**
     * 统一用户名：管理员账号 / 微信openid
     */
    @Override
    public String getUsername() {
        if (isAdmin()) {
            return getSysUser().getUsername();
        } else if (isWxUser()) {
            return getWxUser().getOpenid();
        }
        return "";
    }

    /**
     * 密码：仅管理员有用，微信用户置空
     */
    @Override
    public String getPassword() {
        if (isAdmin()) {
            // 改成实体真实密码字段 getPassword()
            return getSysUser().getPasswordHash();
        }
        // 微信用户无登录密码，返回空
        // 微信用户填固定占位密码，骗过Security校验，不会实际比对
        //登录阶段微信不走密码校验，没用上这个密码
        //鉴权阶段过滤器加载用户时，Security 要读取 password 字段，不为空就不报警告、不抛异常
        return "WX_FIX_PASSWORD_123456";
    }

    // 账号未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账号未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 凭证未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 是否启用
    @Override
    public boolean isEnabled() {
        return true;
    }
}