package com.chaao.appserver.mapper;

import entity.rbac.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import vo.admin.RoleSimpleVO;
import java.util.List;

@Mapper
public interface EmployeeRoleMapper {

    //员工和角色关联(新增用户用到了)
    int insertEmployeeRoleBatch(List<SysUserRole> list);


//    批量查角色（关键点）
//    利用刚才查到的 10 个员工 ID，一次性查出它们所有的角色。
    List<RoleSimpleVO> selectEmployeeRoleBatch( @Param("employeeIds")  List<Long> list);




    //查询用户角色
//    List<RoleSimpleVO> selectEmployeeRole(Long userId);

}
