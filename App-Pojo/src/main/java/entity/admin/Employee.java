package entity.admin;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Employee {

    /**
     * 雪花算法主键
     */
    private Long id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * BCrypt加密密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别 1男 2女
     */
    private Integer sex;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 状态 0禁用 1正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */

    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */

    private Long version;

    /**
     * 逻辑删除 0未删 1已删
     */
    private Integer deleted;
}