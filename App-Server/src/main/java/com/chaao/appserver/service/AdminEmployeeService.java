package com.chaao.appserver.service;

import dto.admin.Employee.EmployeeAllQuery;
import dto.rbac.UserCreateRequest;
import vo.admin.EmployeeVO;

import java.util.List;

public interface AdminEmployeeService {

//    新增员工
    public String addEmployee( UserCreateRequest userCreateRequest );


    //查询所有员工角色
    public List<EmployeeVO> selectAllEmployee( EmployeeAllQuery employeeAllQuery);

}
