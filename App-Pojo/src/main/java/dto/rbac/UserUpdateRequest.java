package dto.rbac;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改用户信息请求参数
 */
@Data
public class UserUpdateRequest {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    private String phone;

    private String email;

    /**
     * 1:正常, 0:禁用
     */
    private Integer status;
}