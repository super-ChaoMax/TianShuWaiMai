package com.chaao.appserver.service;


import dto.rbac.UserCreateRequest;

import java.util.List;
import dto.admin.employee.EmployeeAllQuery;
import vo.admin.EmployeeVO;

public interface AdminEmployeeService {

//    新增员工
    public String addEmployee( UserCreateRequest userCreateRequest );


    //查询所有员工角色
    public List<EmployeeVO> selectAllEmployee(EmployeeAllQuery employeeAllQuery);

}
