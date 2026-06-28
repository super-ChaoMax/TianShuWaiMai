package dto;

import lombok.Data;

@Data
public class AdminLoginDTO {
    /**
     * 管理员账号
     */
    private String username;
    /**
     * 登录密码（前端传明文，后端加密比对）
     */
    private String password;

}