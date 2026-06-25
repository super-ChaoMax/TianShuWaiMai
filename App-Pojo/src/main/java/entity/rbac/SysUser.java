package entity.rbac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RBAC模型：用户表 (sys_user)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
//Lombok 会自动为你的类生成一个内部类（通常叫 SysUserBuilder），
// 并提供链式调用的方法来构建对象。
//SysUser user = SysUser.builder()
//        .username("admin")
//        .passwordHash("xxx")
//        .phone("13800138000")
//        .status(1)
//        .isDeleted(0)
//        .build();

public class SysUser implements Serializable {

    // 主键ID
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 密码哈希值（非明文存储加盐）
     */
    private String passwordHash;

    /**
     * 手机号（不能为空）
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态：1-正常, 0-禁用
     */
    private Integer status;

    /**
     * 逻辑删除标识：0-未删除, 1-已删除
     * 配合 MyBatis-Plus 全局逻辑删除配置使用
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}