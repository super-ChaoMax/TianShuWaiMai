package entity.rbac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用户-角色关联表（多对多）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole {

    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;

    /** 分配时间 */
    private LocalDateTime assignedAt;
}