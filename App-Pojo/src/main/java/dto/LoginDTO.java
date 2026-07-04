package dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginDTO {
    @Schema(description = "登录账号")
    // 账号：管理员账号 / 微信openid
    private String username;
    @Schema(description = "登录密码")
    private String password;
    // 微信登录专用：微信code
    private String code;
    // 拓展字段
    private String verifyCode;
}