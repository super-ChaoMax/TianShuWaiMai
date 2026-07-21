package com.chaao.appserver.mapper.admin;

import dto.admin.employee.EmployeePageQueryDTO;
import entity.admin.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 分页条件查询员工
     */
    List<Employee> pageQuery(@Param("query") EmployeePageQueryDTO query);

    /**
     * 统计总条数
     */
    Long countQuery(@Param("query") EmployeePageQueryDTO query);

    /**
     * 新增员工
     */
    int insert(Employee employee);

    /**
     * 修改员工信息
     */
    int update(Employee employee);

    /**
     * 根据id查询员工
     */
    Employee getById(Long id);

    /**
     * 修改账号状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}