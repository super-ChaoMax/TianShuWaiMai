package dto;

import lombok.Data;

@Data
public class LoginDTO {
    // 账号：管理员账号 / 微信openid
    private String username;
    // 密码：管理员填密码，微信填空字符串
    private String password;
    // 微信登录专用：微信code
    private String code;
    // 拓展字段
    private String verifyCode;
}