package entity.rbac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RBAC模型：角色表 (sys_role)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 角色标识
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 权限版本号（配合高并发乐观锁），变更时+1
     * 乐观锁插件会自动处理：更新时 WHERE version = oldVersion，并 SET version = oldVersion + 1
     */
    private Integer permVersion;

    /**
     * 逻辑删除标识：0-未删除, 1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间（自动填充）
     */
    private LocalDateTime createdAt;
}