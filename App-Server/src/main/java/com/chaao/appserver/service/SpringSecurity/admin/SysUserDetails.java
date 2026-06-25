package com.chaao.appserver.service.SpringSecurity.admin;

import entity.rbac.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


//独立的自定义类（推荐）。创建一个专门的类（如 SysUserDetails）来实现 UserDetails 接口是返回。
// 在 UserDetailsService 的 loadUserByUsername 方法中查出数据库实体后，将其转换为 SysUserDetails 对象返回。
// 这种方式保持了业务实体和安全上下文的解耦，更加灵活。
//解耦：你的数据库实体类（如 SysUser）不需要引入 Spring Security 的依赖（implements UserDetails），保持了业务层的纯净。
public class SysUserDetails implements UserDetails {

    //UserDetails接口的作用
    //UserDetails 仅仅是一个用于存储和封装用户核心信息

    private final SysUser sysUser; // 持有真实的业务用户实体
    private final List<GrantedAuthority> authorities; // 持有权限列表

    public SysUserDetails(SysUser sysUser, List<GrantedAuthority> authorities) {
        this.sysUser = sysUser;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return sysUser.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    // 真实业务中，通常还需要实现账号状态控制（如是否被禁用、锁定等）
    // 这里我们假设用户禁用状态用 isDisabled 表示 是否被禁用
    @Override
    public boolean isAccountNonLocked() {
        return sysUser.getStatus() == 0; // 根据业务字段判断，0 表示禁用，1 表示正常
    }

    // 提供一个快捷方法，方便在 Controller 中获取业务信息
    public SysUser getSysUser() {
        return sysUser;
    }




}
