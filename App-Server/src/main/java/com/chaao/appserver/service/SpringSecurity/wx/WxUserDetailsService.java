package com.chaao.appserver.service.SpringSecurity.wx;

//引入数据库映射接口Mapper

import com.chaao.appserver.mapper.SysUserLoginMapper;
import com.chaao.appserver.mapper.WxUserLoginMapper;
import com.chaao.appserver.service.SpringSecurity.admin.SysUserDetails;
import entity.rbac.SysRole;
import entity.rbac.SysUser;
import entity.wx.WxUser;
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
public class WxUserDetailsService implements UserDetailsService {

    // 微信登录映射的查询语句Mapper（Mapper / Repository）
    @Autowired
    private WxUserLoginMapper wxUserLoginMapper;

    /**
     * 核心方法：根据用户名加载用户信息
     * 这个方法会在【登录验证】和【Token校验】时被 Spring Security 自动调用
     */
    @Override
    public UserDetails loadUserByUsername(String openid) throws UsernameNotFoundException {

        // 1. 查用户
        WxUser wxUser = wxUserLoginMapper.findByOpenid(openid);
        if (wxUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 2. 微信用户目前无权限表
        List<GrantedAuthority> authorities = null;

        return new WxUserDetails(wxUser, authorities);



    }
}