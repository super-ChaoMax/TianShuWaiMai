package com.chaao.appserver.mapper;

import dto.rbac.UserCreateRequest;
import entity.rbac.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminEmployeeMapper {

    //查询是否用户名重复
    int selectByUsername(String username);

    //新增员工
    int insert(UserCreateRequest user );


}
