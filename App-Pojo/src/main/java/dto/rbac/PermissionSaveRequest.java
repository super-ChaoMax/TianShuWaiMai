package dto.rbac;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 新增/修改权限请求参数
 */
@Data
public class PermissionSaveRequest {

    /**
     * 修改时必传，新增时不传
     */
    private Long id;

    @NotBlank(message = "权限标识不能为空")
    private String permCode;

    private String description;
}