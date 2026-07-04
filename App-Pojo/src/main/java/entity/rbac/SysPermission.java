package entity.rbac;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * RBAC模型：权限表 (sys_permission)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 权限标识，如 sys:user:add
     */
    private String permCode;

    /**
     * 对权限的描述
     */
    private String description;
}