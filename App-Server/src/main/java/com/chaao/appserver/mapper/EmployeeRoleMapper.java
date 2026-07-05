package com.chaao.appserver.mapper;

import entity.rbac.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeRoleMapper {

    //员工和角色关联(新增用户用到了)
    int insertEmployeeRoleBatch(List<SysUserRole> list);

}
