package com.chaao.appserver.controller.admin;

import com.chaao.appserver.service.AdminEmployeeService;
import dto.admin.employee.EmployeeAllQuery;
import dto.rbac.UserCreateRequest;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vo.admin.EmployeeVO;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "员工接口模块（后台）")
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private AdminEmployeeService adminEmployeeService; // 2. 注入 Service


    @Operation(summary ="新增员工")
    @PostMapping("/add") // 3. 添加 @PostMapping 定义具体的子路径和请求方式
     public String addEmployee( @Valid @RequestBody @ApiParam("员工新增的参数") UserCreateRequest userCreateRequest) {

        //接收参数
            // 用户名 必填
            // 手机号 必填
            // 密码   得填
            // 性别   1
            // 身份证号

        // 角色（默认），后期修改为可选
            // 状态（默认）



        log.info("接收到新增员工请求: {}", userCreateRequest.getUsername());


        // 5. 调用 Service 层处理业务逻辑
        return adminEmployeeService.addEmployee(userCreateRequest);

    }



//    查询所有的 员工
    @Operation(summary ="查询所有员工")
    @GetMapping()
    public List<EmployeeVO> selectAllEmployee(@ModelAttribute EmployeeAllQuery employeeAllQuery) {
        log.info("查询所有员工");
        return adminEmployeeService.selectAllEmployee(employeeAllQuery);
    }





//    查询单个员工，用占位符 {id}
    @Operation(summary ="查询单个员工")
    @GetMapping("/{id}")
    public EmployeeVO selectEmployeeById(@PathVariable Long id) {
        log.info("查询单个员工");
        return adminEmployeeService.selectEmployeeById(id);
    }


    //测试端口
    @GetMapping("/test")
    public String test() {
        return "hello";
    }

}
