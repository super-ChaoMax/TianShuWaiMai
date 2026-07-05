package com.chaao.appserver.service.Impl.admin;

import com.chaao.appserver.mapper.AdminEmployeeMapper;
import com.chaao.appserver.mapper.EmployeeRoleMapper;
import com.chaao.appserver.service.AdminEmployeeService;
import com.yourcompany.common.util.XueHuaiID;
import dto.admin.employee.EmployeeAllQuery;
import dto.rbac.UserCreateRequest;
import entity.rbac.SysUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import vo.admin.EmployeeVO;
import vo.admin.RoleSimpleVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminEmployeeServiceImpl implements AdminEmployeeService {


//    注入Security密码编码器
    @Autowired
    private PasswordEncoder passwordEncoder;

    //再注入数据层
        //员工层的
        @Autowired
        private AdminEmployeeMapper adminEmployeeMapper;
        // 员工角色层的
        @Autowired
        private EmployeeRoleMapper employeeRoleMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Override
    public String addEmployee(UserCreateRequest userCreateRequest) {

        // 1. 密码加密（不需要放在事务中，因为不涉及数据库操作）
        String encodedPassword = passwordEncoder.encode(userCreateRequest.getPassword());
        userCreateRequest.setPassword(encodedPassword);


        // 2. 使用 TransactionTemplate 执行事务
        return transactionTemplate.execute(status -> {
            try {
                // 3. 业务唯一性校验
                // 注意：通常 selectByUsername 返回的是对象，用 != null 判断更准确
                if (adminEmployeeMapper.selectByUsername(userCreateRequest.getUsername())> 0) {
                    throw new RuntimeException("用户名已存在，请更换");
                }

                // 4. 生成雪花 ID 并设置默认值
                Long newEmployeeId = XueHuaiID.generateUserId(); // 提取为局部变量，方便后续复用
                userCreateRequest.setId(newEmployeeId);
                userCreateRequest.setVersion(1);
                userCreateRequest.setDeleted(0);
                userCreateRequest.setStatus(1);

                // 5. 【关键】先插入员工主表
                adminEmployeeMapper.insert(userCreateRequest);

                // 6. 处理角色关联（防御性编程：防止 roleIds 为 null 导致空指针）
                List<Long> roleIds = userCreateRequest.getRoleIds();
                if (roleIds == null || roleIds.isEmpty()) {
                    // 如果前端未传角色，自动分配默认角色（假设普通员工角色ID为 1L）
                    roleIds = Collections.singletonList(1620010000000000002L);
                }

                // 7. 构建中间表实体并批量插入
                List<SysUserRole> relations = roleIds.stream().map(roleId -> {

                    //这里的作用是把角色绑定到员工

                    SysUserRole er = new SysUserRole();
                    er.setId(XueHuaiID.generateUserId());
                    er.setUserId(newEmployeeId); // 使用刚才生成的局部变量
                    er.setRoleId(roleId);
                    return er;
                }).collect(Collectors.toList());

                // 【关键】在员工主表插入之后，再插入角色关联表
                employeeRoleMapper.insertEmployeeRoleBatch(relations);

                return "员工创建成功";

            } catch (Exception e) {
                // 捕获异常后，手动标记事务回滚
                status.setRollbackOnly();
                log.error("新增员工失败", e);
                return "创建失败：" + e.getMessage();
            }
        });


    }



//    查询所有员工
    @Override
    public List<EmployeeVO> selectAllEmployee(EmployeeAllQuery employeeAllQuery) {

        //查询所有的员工
        List<EmployeeVO> employeeList = adminEmployeeMapper.selectAllEmployee(employeeAllQuery);


        // 防御性编程：如果没查到员工，直接返回，避免下面报错
        if (employeeList == null || employeeList.isEmpty()) {
            return employeeList;
        }



        // 2. 提取 ID 列表
        List<Long> employeeIds = employeeList.stream()
                .map(EmployeeVO::getId)
                .collect(Collectors.toList());

        // 3. 批量查询角色
        // 注意：这里查出来的 roleList 是扁平的，比如：
        // [{roleId:1, employeeId:100}, {roleId:2, employeeId:100}, {roleId:3, employeeId:101}]
        List<RoleSimpleVO> roleList = employeeRoleMapper.selectEmployeeRoleBatch(employeeIds);

        // 4. 使用 Map 将角色归类 (关键修改点)
        // 必须按照【员工ID】(employeeId) 进行分组，而不是角色ID
        Map<Long, List<RoleSimpleVO>> roleMap = roleList.stream()
                .collect(Collectors.groupingBy(RoleSimpleVO::getEmployeeId));

        // 5. 组装数据
        for (EmployeeVO emp : employeeList) {
            // 从 Map 中取出该员工对应的角色列表塞进去
            // getOrDefault 保证如果没有角色，就塞一个空列表，防止前端报空指针
            emp.setRoles(roleMap.getOrDefault(emp.getId(), Collections.emptyList()));
        }

        // 6. 返回组装好的员工列表 (关键修改点)
        return employeeList;


    }




//    单个查询
    @Override
    public EmployeeVO selectEmployeeById(Long userId) {
        log.info("查询单个员工");
        EmployeeVO employeeVO = adminEmployeeMapper.selectEmployeeById(userId);
//        log.info("查询单个员工，结果：{}", employeeVO);
        //因为
        // e. 开头 -> 查 employee 表
        //r. 开头 -> 查 sys_role 表
        //er. 开头 -> 查 sys_employee_role 关联表
        //我们需要Employee_id，但是我们没有查询查 sys_employee_role 关联表，那我们自己把传进来的ID给他加一下
            //给List<RoleSimpleVO> roles循环添加employee_id
        employeeVO.getRoles().forEach(role -> role.setEmployeeId(userId));



        return employeeVO;
    }





}
