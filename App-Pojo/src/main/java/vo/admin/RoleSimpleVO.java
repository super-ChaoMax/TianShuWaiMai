package vo.admin;

import lombok.Data;

@Data
public class RoleSimpleVO {
    //给前端显示
    //id (Long)：必传。前端在后续修改该员工、取消或新增角色时，必须依赖这个 ID 来和后端进行交互。
    //roleName / name (String)：必传。用于前端页面直接展示（例如标签、下拉框选项）。
    //roleCode / code (String)：强烈建议传。前端经常需要根据角色的唯一标识（如 ROLE_ADMIN）来进行 UI 按钮级别的权限控制（比如：if (roleCode === 'ROLE_ADMIN') 显示删除按钮）。
    private Long id;
    private String roleName;
    private String roleCode;

    // 必须补充 employeeId 字段
    //因为我们在 Service 层需要把查出来的角色列表按员工 ID 分组（Collectors.groupingBy(RoleSimpleVO::getEmployeeId)），所以这个 VO 里必须得知道它属于哪个员工。
    /**
     * 【新增字段】：关联的员工ID
     * 作用：在 Service 层批量查询角色后，用于按员工 ID 进行分组 (groupingBy)
     */
    private Long employeeId;
}