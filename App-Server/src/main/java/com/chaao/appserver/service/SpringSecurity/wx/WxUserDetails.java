package com.chaao.appserver.service.SpringSecurity.wx;

import entity.wx.WxUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 微信用户 安全用户详情
 * 专门给微信登录使用
 */
public class WxUserDetails implements UserDetails {

    // 微信业务实体
    private final WxUser wxUser;
    // 微信用户权限
    private final Collection<? extends GrantedAuthority> authorities;

    // 构造方法
    public WxUserDetails(WxUser wxUser, Collection<? extends GrantedAuthority> authorities) {
        this.wxUser = wxUser;
        this.authorities = authorities;
    }

    // 获取权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 微信没有密码，返回空
    @Override
    public String getPassword() {
        // 微信登录无密码
        return "";
    }

    // 微信登录唯一标识：openid 当作用户名
    @Override
    public String getUsername() {
        return wxUser.getOpenid();
    }

    // 账号是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账号是否未锁定
    @Override
    public boolean isAccountNonLocked() {
        // 1正常 0禁用
        return wxUser.getStatus() == 1;
    }

    // 凭证未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 是否启用
    @Override
    public boolean isEnabled() {
        return wxUser.getStatus() == 1;
    }

    // ========== 自己提供快捷方法，拿微信用户信息 ==========
    public WxUser getWxUser(){
        return this.wxUser;
    }

    // 获取微信用户ID
    public Long getWxUserId(){
        return wxUser.getId();
    }
}