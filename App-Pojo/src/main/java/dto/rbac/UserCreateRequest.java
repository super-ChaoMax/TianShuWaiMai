package dto.rbac;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 创建用户请求参数
 */
@Data
public class UserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    private String email;

    /**
     * 创建用户时，可同时分配角色
     */
    private List<Long> roleIds;
}