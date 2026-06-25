package com.chaao.appserver.service.SpringSecurity.admin;

//引入数据库映射接口Mapper
import com.chaao.appserver.mapper.SysUserLoginMapper;


//引入数据库的实体类（用户）
import entity.rbac.SysRole;
import entity.rbac.SysUser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义用户详情服务
 * 实现 UserDetailsService 接口，用于从数据库加载用户信息
 *
 *  loadUserByUsername的返回类型是 UserDetails， 所以你需要返回一个 UserDetails 对象
 */
@Service
public class SysUserDetailsService implements UserDetailsService {

    // 登录映射的查询语句Mapper（Mapper / Repository）
    @Autowired
    private SysUserLoginMapper sysUserLoginMapper;

    /**
     * 核心方法：根据用户名加载用户信息
     * 这个方法会在【登录验证】和【Token校验】时被 Spring Security 自动调用
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
/*
        // 1. 从数据库查询用户
        SysUser sysUser = loginMapper.findByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 2. 构建权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 2.1 查询并添加角色
        List<SysRole> roles = loginMapper.getRolesByUserId(sysUser.getId());
        if (roles != null && !roles.isEmpty()) {
            // 将角色转换为权限字符串，通常加上 ROLE_ 前缀以便区分（必须你的数据库自己没加ROLE_ 前缀）
            List<SimpleGrantedAuthority> roleAuthorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()))
                    .toList();
            authorities.addAll(roleAuthorities);

            // 2.2 查询并添加权限 (优化：建议在 Mapper 层写一个 findPermsByUserId(id) 直接查出所有权限)
            // 这里演示如何在 Java 层处理多角色情况
            for (SysRole role : roles) {
                List<String> perms = loginMapper.findPermissionsByRoleId(role.getId());
                if (perms != null) {
                    authorities.addAll(perms.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList());
                }
            }
        }

        // 3. 向上转型为接口类型，解决泛型不兼容问题
        List<GrantedAuthority> authList = new ArrayList<>(authorities);

        // 4. 返回自定义的 UserDetails 对象
        return new SysUserDetails(sysUser, authList);
*/

        // 1. 查用户
        SysUser sysUser = sysUserLoginMapper.findByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 2. 一次性查出所有权限（包含角色和细粒度权限）


        List<SysRole> roles = sysUserLoginMapper.findRolesByUserId(sysUser.getId());
        List<String> permCodes = sysUserLoginMapper.findPermsByUserId(sysUser.getId());

        // 3. 转换并构建 UserDetails
        List<GrantedAuthority> authorities = permCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()))
                .toList());

        return new SysUserDetails(sysUser, authorities);



    }
}