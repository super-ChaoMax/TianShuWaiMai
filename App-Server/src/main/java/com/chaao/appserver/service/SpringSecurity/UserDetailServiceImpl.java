package com.chaao.appserver.service.SpringSecurity;

import com.chaao.appserver.mapper.SysUserLoginMapper;
import com.chaao.appserver.mapper.WxUserLoginMapper;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
//问题 1：finally 提前清空 ThreadLocal 重大 BUG
//运行
//finally {
//    LOGIN_TYPE.remove();
//}
//执行流程：
//进入微信分支 type=2
//查到 wxUser
//还没 return 对象，直接进 finally 把 LOGIN_TYPE 删了
//后续构建、上下文绑定都拿不到类型，直接错乱
//致命错误，必须删掉这个 finally 里的 remove

    @Autowired
    private SysUserLoginMapper sysUserMapper;
    @Autowired
    private WxUserLoginMapper wxUserMapper;

    // 本地线程存放登录类型，区分走哪个表
    public static final ThreadLocal<Integer> LOGIN_TYPE = new ThreadLocal<>();

    @Override
    public UserDetails loadUserByUsername(String username) {
        Integer type = LOGIN_TYPE.get();
        List<GrantedAuthority> auths;

            // 1 管理员登录
            if (Integer.valueOf(1).equals(type)) {
                SysUser sysUser = sysUserMapper.findByUsername(username);
                if (sysUser == null) {
                    throw new UsernameNotFoundException("管理员账号不存在");
                }
                // 查询角色 + 权限
                List<SysRole> roles = sysUserMapper.findRolesByUserId(sysUser.getId());
                List<String> permCodes = sysUserMapper.findPermsByUserId(sysUser.getId());

                // 封装角色
                auths = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()))
                        .collect(Collectors.toList());

                // 封装权限
                if (permCodes != null && !permCodes.isEmpty()) {
                    auths.addAll(permCodes.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
                }
                return LoginUser.buildAdmin(sysUser, auths);
            }

            // 2 微信用户登录(根据openid查询)
            else if (Integer.valueOf(2).equals(type)) {
                WxUser wxUser = wxUserMapper.findByOpenid(username);
                if (wxUser == null) {
                    throw new UsernameNotFoundException("微信用户不存在");
                }
                auths = Collections.singletonList(new SimpleGrantedAuthority("ROLE_WX_USER"));
                return LoginUser.buildWxUser(wxUser, auths);
            }
            throw new UsernameNotFoundException("未知登录类型");

         /*
         finally {
            // 线程变量强制清空，防止内存泄露、登录类型错乱

            LOGIN_TYPE.remove();
            //这里删除会有问题
            //走到 return LoginUser.buildWxUser(wxUser, auths);
            //优先执行 finally → LOGIN_TYPE.remove () 清空
            //才把 LoginUser 对象返回
            //后续同线程其他逻辑再拿 LOGIN_TYPE.get() → null
            //直接登录类型错乱、权限异常、偶发报错。

            //----请求整个链路走完再清，中途绝对不删统一放在    JWT 过滤器 finally 清理（推荐）


        }
        */

    }
}