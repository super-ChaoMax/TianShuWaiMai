package com.chaao.appserver.controller.admin;

import com.chaao.appserver.service.EmployeeService;
import dto.admin.employee.EmployeeDTO;
import dto.admin.employee.EmployeePageQueryDTO;
import dto.admin.employee.EmployeeStatusDTO;
import entity.admin.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.PageResult;
import vo.Result;

@RestController
@RequestMapping("/admin/employee")
@Tag(name = "员工接口模块（后台）")
@Slf4j
public class newEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工分页查询
     */
    @Operation(summary ="新增员工")
    @GetMapping("/page")
    public Result<PageResult<Employee>> page(EmployeePageQueryDTO dto){
        // service 返回就是你 vo 的 PageResult
        PageResult<Employee> pageData = employeeService.page(dto);
        return Result.success(pageData);
    }

    /**
     * 新增员工
     */
    @Operation(summary ="新增员工")
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDTO dto){
        employeeService.save(dto);
        return Result.success("新增成功");
    }

    /**
     * 修改员工
     */
    @Operation(summary ="修改员工")
    @PutMapping
    public Result<String> update(@RequestBody EmployeeDTO dto){
        employeeService.update(dto);
        return Result.success("修改成功");
    }

    /**
     * 根据id查询员工
     */
    @Operation(summary ="根据id查询员工")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 启用/禁用员工
     */
    @Operation(summary ="启用/禁用员工")
    @PostMapping("/status")
    public Result<String> updateStatus(@RequestBody EmployeeStatusDTO dto){
        employeeService.updateStatus(dto);
        return Result.success("状态更新成功");
    }
}