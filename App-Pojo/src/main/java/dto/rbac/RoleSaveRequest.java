package dto.rbac;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * 新增/修改角色请求参数
 */
@Data
public class RoleSaveRequest {

    /**
     * 修改时必传，新增时不传
     */
    private Long id;

    @NotBlank(message = "角色标识不能为空")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 该角色拥有的权限ID列表
     */
    private List<Long> permIds;
}