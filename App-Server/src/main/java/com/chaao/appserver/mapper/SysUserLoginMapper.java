package com.chaao.appserver.mapper;

import entity.rbac.SysRole;
import entity.rbac.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 登录数据库映射接口
@Mapper
public interface SysUserLoginMapper {

    // 根据用户名查询用户
    SysUser findByUsername(String username);

    // 根据用户ID查询用户的角色
    List<SysRole> findRolesByUserId(Long userId);

    // 根据用户ID查询用户的权限(只需要返回去权限列表)
    List<String> findPermsByUserId(Long userId);

}
