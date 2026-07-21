package com.chaao.appserver.service.Impl.admin;

import cn.hutool.core.bean.BeanUtil;

import com.chaao.appserver.mapper.admin.EmployeeMapper;
import com.chaao.appserver.service.EmployeeService;
import com.yourcompany.common.util.XueHuaiID;
import dto.admin.employee.EmployeeDTO;
import dto.admin.employee.EmployeePageQueryDTO;
import dto.admin.employee.EmployeeStatusDTO;
import entity.admin.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vo.PageResult;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    // BCrypt密码加密
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public PageResult<Employee> page(EmployeePageQueryDTO dto) {
        // 计算分页起始索引
        int start = (dto.getPage() - 1) * dto.getPageSize();
        dto.setPage(start);
        List<Employee> list = employeeMapper.pageQuery(dto);
        Long total = employeeMapper.countQuery(dto);
        return new PageResult<>(total, list);
    }

    @Override
    public void save(EmployeeDTO dto) {
        Employee employee = BeanUtil.copyProperties(dto, Employee.class);
        // 密码加密
        String encodePwd = passwordEncoder.encode(dto.getPassword());
        employee.setPassword(encodePwd);
        // 默认正常状态
        employee.setStatus(1);

        //并且设置id
        employee.setId(XueHuaiID.generateUserId());

        employeeMapper.insert(employee);
    }

    @Override
    public void update(EmployeeDTO dto) {
        Employee employee = BeanUtil.copyProperties(dto, Employee.class);
        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    @Override
    public void updateStatus(EmployeeStatusDTO dto) {
        employeeMapper.updateStatus(dto.getId(), dto.getStatus());
    }
}