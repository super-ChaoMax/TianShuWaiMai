package com.chaao.appserver.service;

import dto.admin.employee.EmployeeDTO;
import dto.admin.employee.EmployeePageQueryDTO;
import dto.admin.employee.EmployeeStatusDTO;
import entity.admin.Employee;
import vo.PageResult;

public interface EmployeeService {

    /**
     * 员工分页查询
     */
    PageResult<Employee> page(EmployeePageQueryDTO dto);

    /**
     * 新增员工
     */
    void save(EmployeeDTO dto);

    /**
     * 修改员工信息
     */
    void update(EmployeeDTO dto);

    /**
     * 根据id查询员工
     */
    Employee getById(Long id);

    /**
     * 启用禁用员工
     */
    void updateStatus(EmployeeStatusDTO dto);
}