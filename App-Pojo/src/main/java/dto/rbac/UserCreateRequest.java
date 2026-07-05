package dto.rbac;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 系统员工用户请求参数 DTO
 * 仅接收前端需要传递的业务字段，隔离敏感字段（如id, version, deleted）
 */
@Data
@ApiModel(description = "系统员工用户请求参数")
public class UserCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    //ID
    @Schema(description = "ID")
    private Long id;
    // Version
    @Schema(description = "Version")
    private Integer version;
    // Deleted
    @Schema(description = "Deleted")
    private Integer deleted;

    /**
     * 用户名（唯一）
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", required = true)
    private String username;

    /**
     * 密码（新增时必填，修改时选填；后端需进行加密处理）
     */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码（新增时必填，修改时选填）")
    private String password;



    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Schema(description = "昵称")
    private String name;


    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号", required = true)
    private String phone;

    /**
     * 性别：1-男, 2-女, null-未知
     */
    @Schema(description = "性别：1-男, 2-女")
    private Integer sex;

    /**
     * 员工身份证号
     */
    @Schema(description = "员工身份证号")
    private String idNumber;

    /**
     * 状态：1-正常, 0-禁用/离职   默认
     */
//    @NotNull(message = "员工状态不能为空")
    @Schema(description = "状态：1-正常, 0-禁用", required = true)
    private Integer status;


    /**
     * 创建用户时，可同时分配角色
     */
    private List<Long> roleIds;

}