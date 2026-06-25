package entity.rbac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 角色-权限关联表（多对多）
 * 不使用物理外键，通过业务逻辑保证一致性
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID（联合主键之一）
     */
    private Long roleId;

    /**
     * 权限ID（联合主键之一）
     */
    private Long permId;
}