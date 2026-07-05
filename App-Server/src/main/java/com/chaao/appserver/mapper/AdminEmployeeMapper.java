package com.chaao.appserver.mapper;



import dto.rbac.UserCreateRequest;
import org.apache.ibatis.annotations.Mapper;

import vo.admin.EmployeeVO;
import dto.admin.employee.EmployeeAllQuery;
import java.util.List;

@Mapper
public interface AdminEmployeeMapper {

    //查询是否用户名重复
    int selectByUsername(String username);

    //新增员工
    int insert(UserCreateRequest user );



    //修改员工的同时就要回显员工所拥有的角色

      //查询所有员工信息（需要分页的参数）
      List<EmployeeVO> selectAllEmployee(EmployeeAllQuery employeeAllQuery);

      //查询单个员工信息
//      EmployeeVO selectEmployeeById(Long userId);

}
