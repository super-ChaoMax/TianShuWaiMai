package com.chaao.appserver.service;

import dto.rbac.UserCreateRequest;

public interface AdminEmployeeService {

//    新增员工
    public String addEmployee( UserCreateRequest userCreateRequest );

}
