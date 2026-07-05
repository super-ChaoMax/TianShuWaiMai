package vo.admin;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeVO {
    // 1. 员工基础信息字段（按需挑选，不要直接复制所有字段）
    private Long id;
    private String username;
    private String name;
    private String phone;
    private Integer sex;
    private String idNumber;
    private Integer status;
    private String createTime;
    private String updateTime;
    
    // 2. 关联的角色列表
    private List<RoleSimpleVO> roles;


}